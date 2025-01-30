package tools.vitruv.vitruvAdapter.core.impl.sourceCodeView

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Enumeration
import org.eclipse.uml2.uml.Interface
import org.eclipse.uml2.uml.OpaqueBehavior
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.UMLFactory
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.TextDisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.mapper.TextViewMapper
import java.util.function.Consumer
import kotlin.collections.forEach

/**
 * This class maps java classes to a string source code representation
 */
class SourceCodeViewMapper: TextViewMapper() {

    override fun  mapEObjectsToWindowsContent(rootObjects: List<EObject>): List<Window<String>> {
        val windows = mutableSetOf<Window<String>>()
        rootObjects.forEach {
            if(it is Class){
                val window = Window(it.name, createSourceCodeForClass(it))
                windows.add(window)
            }
            if (it is Interface){
                val window = Window(it.name, createSourceCodeForInterface(it))
                windows.add(window)
            }
            if (it is Enumeration){
                val window = Window(it.name, createSourceCodeForEnum(it))
                windows.add(window)
            }

        }
        return windows.toList()
    }

    /**
     * Maps the given json string to view content, compares it to [oldEObjects] and applies the changes to [oldEObjects].
     * Note that no changes will be applied to the model,
     * this have to be done after this method with a View object, where the [oldEObjects] came from.
     * @param oldEObjects The old EObjects to compare the windows to.
     * @param windows the windows to map to EObjects.
     * @return The view content.
     */
    override fun mapWindowsToEObjectsAndApplyChangesToEObjects(
        oldEObjects: List<EObject>,
        windows: List<Window<String>>
    ): List<EObject> {
        TODO("Not yet implemented")
    }

    private fun createSourceCodeForEnum(enumObject: Enumeration): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("enum ${enumObject.name} {\n")
        stringBuilder.append("\n}")
        //TODO: Add enum values
        return stringBuilder.toString()
    }

    private fun createSourceCodeForInterface(interfaceObject: Interface): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("interface ${interfaceObject.name} {\n")
        stringBuilder.append(addTabSpacing(buildMethodsForInterface(interfaceObject)))
        stringBuilder.append("\n}")
        return stringBuilder.toString()
    }

    private fun buildMethodsForInterface(interfaceObject: Interface): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n")
        interfaceObject.ownedOperations.forEach { operation ->
            stringBuilder.append(
                "${operation.visibility?.literal ?: "Unknown"} ${operation.type?.name ?: "Unknown"} ${operation.name}" +
                        "(${
                            operation.ownedParameters
                                .filter { it.direction == ParameterDirectionKind.IN_LITERAL }
                                .joinToString { "${it.type?.name ?: "Unknown"} ${it.name}" }
                        }) { \n"
            )
        }
        stringBuilder.append("\n")
        stringBuilder.append("}")
        return stringBuilder.toString()
        }



    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        val windows = mutableSetOf<String>()
        rootObjects.forEach {
            if (it is Package) {
                val iterator = it.eAllContents()
                while (iterator.hasNext()) {
                    when (val eObject = iterator.next()) {
                        is Class -> windows.add(eObject.name)
                        is Interface -> windows.add(eObject.name)
                        is Enumeration -> windows.add(eObject.name)
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
            if (!operation.methods.isEmpty()) {
                val behavior = operation.methods[0] as OpaqueBehavior
                for (i in behavior.bodies.indices) {
                    val body = behavior.bodies[i]
                    stringBuilder.append(addTabSpacing(body.trimIndent()))
                }
            }
            stringBuilder.append("\n")
            stringBuilder.append("}")
        }

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