package tools.vitruv.vitruvAdapter.vitruv.impl.mapper

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.*
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.vitruv.api.Window
import tools.vitruv.vitruvAdapter.vitruv.impl.displayContent.TextDisplayContentMapper
import java.util.function.Consumer


/**
 * This class maps java classes to a string source code representation
 */
class SourceCodeViewMapper: TextViewMapper() {

    override fun  mapEObjectsToWindowsContent(rootObjects: List<EObject>): List<Window<String>> {
        val windows = mutableSetOf<Window<String>>()
        rootObjects.forEach {
            if (it is Package) {
                it.packagedElements.forEach { element ->
                    if (element is Class) {
                        val window = Window(element.name, createSourceCodeForClass(element))
                        windows.add(window)
                    }
                }
            }
            if(it is Class){
                val window = Window(it.name, createSourceCodeForClass(it))
                windows.add(window)
            }
        }
        return windows.toList()
    }

    override fun mapWindowsContentToEObjects(windows: List<Window<String>>): List<EObject> {
        val umlPackage = UMLFactory.eINSTANCE.createPackage()

        for (window in windows) {
            // Parse the file
            val compilationUnit: CompilationUnit = StaticJavaParser.parse(window.content)

            // Find the class (assuming there's only one top-level class)
            val classDeclaration: ClassOrInterfaceDeclaration = compilationUnit
                .findFirst(ClassOrInterfaceDeclaration::class.java)
                .orElseThrow { RuntimeException("No class found in the file.") }


            // Now you can collect methods and fields
            val methods = collectMethods(classDeclaration)
            val fields = collectFields(classDeclaration)
            val classObject = UMLFactory.eINSTANCE.createClass()
            classObject.name = classDeclaration.nameAsString
            fields.forEach { field ->
                val attribute = UMLFactory.eINSTANCE.createProperty()
                attribute.name = field.variables[0].nameAsString
                attribute.type = UMLFactory.eINSTANCE.createPrimitiveType()
                attribute.type.name = field.elementType.asString()
                val stringValue = UMLFactory.eINSTANCE.createLiteralString()
                stringValue.value = field.variables[0].initializer.get().toString()
                attribute.defaultValue = stringValue
                classObject.ownedAttributes.add(attribute)

            }
            methods.forEach { method ->
                val operation = UMLFactory.eINSTANCE.createOperation()
                operation.name = method.nameAsString
                operation.type = UMLFactory.eINSTANCE.createPrimitiveType()
                operation.type.name = method.typeAsString
                method.parameters.forEach { parameter ->
                    val parameterObject = UMLFactory.eINSTANCE.createParameter()
                    parameterObject.name = parameter.nameAsString
                    parameterObject.type = UMLFactory.eINSTANCE.createPrimitiveType()
                    parameterObject.type.name = parameter.typeAsString
                    operation.ownedParameters.add(parameterObject)
                }
                val body = UMLFactory.eINSTANCE.createOpaqueBehavior()
                body.name = method.nameAsString
                body.bodies.add(method.body.get().toString())
                operation.methods.add(body)
                classObject.ownedOperations.add(operation)
            }
            umlPackage.packagedElements.add(classObject)
        }
        return listOf(umlPackage)
    }


    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        val windows = mutableSetOf<String>()
        rootObjects.forEach {
            if (it is Package) {
                it.packagedElements.forEach { element ->
                    if (element is Class) {
                        windows.add(element.name)
                    }
                    if(element is Interface){
                        windows.add(element.name)
                    }
                }
            }
        }
        return windows
    }

    override fun getDisplayContent(): DisplayContentMapper<String> {
        return TextDisplayContentMapper()
    }

    private fun createSourceCodeForClass(classObject: Class): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("class ${classObject.name} {\n")
        stringBuilder.append(addTabSpacing(buildStringContainingAttributes(classObject)))
        stringBuilder.append(addTabSpacing(buildMethods(classObject)))
        stringBuilder.append("\n}")
        return stringBuilder.toString()
    }


    private fun buildStringContainingAttributes(classObject: Class) : String {
        val stringBuilder = StringBuilder()
        val staticAttributes = classObject.ownedAttributes.filter { it.isStatic }
        val nonStaticAttributes = classObject.ownedAttributes.filter { !it.isStatic }



        staticAttributes.forEach {
            stringBuilder.append("${it.visibility.literal} static ${it.type?.name ?: "Unknown"} ${it.name} = ${it.defaultValue?.stringValue() ?: "Unknown"};")
            stringBuilder.append("\n")
        }
        nonStaticAttributes.forEach {
            stringBuilder.append("${it.visibility.literal} ${it.type?.name ?: "Unknown"} ${it.name} = ${it.defaultValue?.stringValue() ?: "Unknown"};")
            stringBuilder.append("\n")
        }


        return stringBuilder.toString()
    }

    private fun buildMethods(classObject: Class): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n")
        classObject.ownedOperations.forEach { operation ->
            stringBuilder.append(
                "${operation.visibility?.literal ?: "Unknown"} ${operation.type?.name ?: "Unknown"} ${operation.name}" +
                        "(${operation.ownedParameters
                            .filter { it.direction == ParameterDirectionKind.IN_LITERAL }
                            .joinToString { "${it.type?.name ?: "Unknown"} ${it.name}" }}) { \n"
            )
            //build the body of the method
            if (operation.methods.isEmpty()) {
                return stringBuilder.toString()
            }
            val behavior = operation.methods[0] as OpaqueBehavior
            for (i in behavior.bodies.indices) {
                val body = behavior.bodies[i]
                stringBuilder.append(addTabSpacing(body.trimIndent()))
            }
        }


        stringBuilder.append("\n")
        stringBuilder.append("}")
        return stringBuilder.toString()
    }
    private fun addTabSpacing(code: String): String {
        return code.lines()
            .joinToString("\n") { "\t$it" } // Add a tab character before each line
    }

    private fun collectMethods(classDeclaration: ClassOrInterfaceDeclaration): List<MethodDeclaration> {
        val methods: MutableList<MethodDeclaration> = ArrayList()
        classDeclaration.methods.forEach(Consumer { method: MethodDeclaration ->
            methods.add(method)
        })
        return methods
    }

    private fun collectFields(classDeclaration: ClassOrInterfaceDeclaration): List<FieldDeclaration> {
        val fields: MutableList<FieldDeclaration> = ArrayList()
        classDeclaration.fields.forEach(Consumer { field: FieldDeclaration ->
            fields.add(field)
        })
        return fields


    }


}
