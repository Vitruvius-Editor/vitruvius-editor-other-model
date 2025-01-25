package tools.vitruv.vitruvAdapter.vitruv.impl.mapper

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.uml2.uml.*
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.vitruv.api.Window
import tools.vitruv.vitruvAdapter.vitruv.impl.displayContent.TextDisplayContentMapper


/**
 * This class maps java classes to a string source code representation
 */
class SourceCodeViewMapper: TextViewMapper() {

    override fun mapEObjectsToWindowsContent(rootObjects: List<EObject>): List<Window<String>> {
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
        }
        return windows.toList()
    }

    override fun mapWindowsContentToEObjects(windows: List<Window<String>>): List<EObject> {
        TODO("Not yet implemented")
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
            stringBuilder.append("${it.visibility.literal} static ${it.type.name} ${it.name} = ${it.defaultValue.stringValue()}")
            stringBuilder.append("\n")
        }
        nonStaticAttributes.forEach {
            stringBuilder.append("${it.visibility.literal} ${it.type.name} ${it.name} = ${it.defaultValue.stringValue()}")
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    private fun buildMethods(classObject: Class): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n")
        classObject.ownedOperations.forEach { operation ->
            stringBuilder.append(
                "${operation.visibility.literal} ${operation.type.name} ${operation.name}" +
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


}
