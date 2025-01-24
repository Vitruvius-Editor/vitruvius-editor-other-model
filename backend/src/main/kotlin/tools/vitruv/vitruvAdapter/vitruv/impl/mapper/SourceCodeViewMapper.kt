package tools.vitruv.vitruvAdapter.vitruv.impl.mapper

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.*
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.vitruv.api.Window


/**
 * This class maps java classes to a string source code representation
 */
class SourceCodeViewMapper: TextViewMapper() {

    override fun mapEObjectsToWindowsContent(rootObjects: List<EObject>): List<Window<String>> {
        val windows = mutableSetOf<Window<String>>()
        rootObjects.forEach {
            if (it is Class) {
                val window = Window(it.name, createSourceCodeForClass(it))
                windows.add(window)
            }
        }
        return windows.toList()
    }

    override fun mapWindowsContentToEObjects(windows: List<Window<String>>): List<EObject> {
        TODO("Not yet implemented")
    }

    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        TODO("Not yet implemented")
    }

    override fun getDisplayContent(): DisplayContentMapper<String> {
        TODO("Not yet implemented")
    }

    private fun createSourceCodeForClass(classObject: Class): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("class ${classObject.name} {\n")
        buildStringContainingAttributes(classObject, stringBuilder)
        buildMethods(classObject, stringBuilder)
        stringBuilder.append("\n}")
        return stringBuilder.toString()
    }


    private fun buildStringContainingAttributes(classObject: Class, stringBuilder: StringBuilder) {
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
    }

    private fun buildMethods(classObject: Class, stringBuilder: StringBuilder) {
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
                return
            }
            val behavior = operation.methods[0] as OpaqueBehavior
            for (i in behavior.bodies.indices) {
                val body = behavior.bodies[i]
                stringBuilder.append(addTabSpacing(body.trimIndent()))
            }
        }
    }
    private fun addTabSpacing(code: String): String {
        return code.lines()
            .joinToString("\n") { "\t$it" } // Add a tab character before each line
    }


}
