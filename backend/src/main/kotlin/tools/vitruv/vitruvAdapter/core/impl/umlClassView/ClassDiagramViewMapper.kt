package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Classifier
import org.eclipse.uml2.uml.Interface
import org.eclipse.uml2.uml.Package
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.abstractMapper.UmlViewMapper
import tools.vitruv.vitruvAdapter.core.impl.uml.*

class ClassDiagramViewMapper : UmlViewMapper() {
    /**
     * Maps the given view content to a list of windows.
     * @param selectEObjects The view content to map.
     * @return The windows representing the view content.
     */
    override fun mapEObjectsToWindowsContent(selectEObjects: List<EObject>): List<Window<UmlDiagram>> {
        val windows = mutableListOf<Window<UmlDiagram>>()
        for (eObject in selectEObjects) {
            if (eObject !is Package) {
                continue
            }
            val umlDiagram = mapPackageToUmlDiagram(eObject)
            val window = Window(eObject.name, umlDiagram)
            windows.add(window)
        }
        return windows
    }

    private fun mapPackageToUmlDiagram(eObject: Package): UmlDiagram {
        val nodes = mutableListOf<UmlNode>()
        val connections = mutableListOf<UmlConnection>()
        val iterator = eObject.eAllContents()
        val resource = eObject.eResource()
        while (iterator.hasNext()) {
            val next = iterator.next()
            val nextURI = resource?.getURIFragment(next) ?: ""
            if (next is Class) {
                val isAbstract = if (next.isAbstract) "<<abstract>>" else "<<class>>"
                val classNode =
                    UmlNode(nextURI, next.name, isAbstract, getUmlAttributes(next), getUmlMethods(next))
                nodes.add(classNode)


                if (next.interfaceRealizations.isNotEmpty()) {
                    next.interfaceRealizations.forEach { interfaceRealization ->
                        val umlConnection = UmlConnection(
                            resource?.getURIFragment(interfaceRealization) ?: "",
                            resource?.getURIFragment(next) ?: "",
                            resource?.getURIFragment(interfaceRealization.contract) ?: "",
                            UmlConnectionType.IMPLEMENTS,
                            "",
                            "",
                            ""
                        )
                        connections.add(umlConnection)
                    }
                }

                if (next.superClasses.isNotEmpty()) {
                    val superClass = next.superClasses.first()
                    val umlConnection = UmlConnection(
                        resource?.getURIFragment(superClass) ?: "",
                        resource?.getURIFragment(next) ?: "",
                        resource?.getURIFragment(superClass) ?: "",
                        UmlConnectionType.EXTENDS,
                        "",
                        "",
                        ""
                    )
                    connections.add(umlConnection)
                }
            }
            if (next is Interface) {
                val interfaceNode =
                    UmlNode(nextURI, next.name, "<<interface>>", getUmlAttributes(next), getUmlMethods(next))
                nodes.add(interfaceNode)
            }

        }
        return UmlDiagram(nodes, connections)
    }

    private fun getUmlAttributes(next: Classifier): List<UmlAttribute> {
        val umlAttributes = mutableListOf<UmlAttribute>()
        next.attributes.forEach {
            val visibilitySymbol = getVisibilitySymbol(it.visibility.literal.lowercase())
            val umlVisibility = UmlVisibility.fromSymbol(visibilitySymbol) ?: UmlVisibility.PUBLIC
            umlAttributes.add(UmlAttribute(umlVisibility, it.name, it.type.name))
        }
        return umlAttributes
    }

    private fun getUmlMethods(next: Classifier): List<UmlMethod> {
        val umlMethods = mutableListOf<UmlMethod>()
        next.operations.forEach {
            val visibilitySymbol = getVisibilitySymbol(it.visibility.literal.lowercase())
            val umlVisibility = UmlVisibility.fromSymbol(visibilitySymbol) ?: UmlVisibility.PUBLIC
            val umlParameters = mutableListOf<UmlParameter>()
            it.ownedParameters.forEach { parameter ->
                umlParameters.add(UmlParameter(parameter.name, parameter.type.name))
            }
            umlMethods.add(UmlMethod(umlVisibility, it.name, umlParameters, it.type.name))
        }
        return umlMethods
    }

    private fun getVisibilitySymbol(visibility: String): String {
        return when (visibility) {
            "public" -> "+"
            "private" -> "-"
            "protected" -> "#"
            "package" -> "~"
            else -> ""
        }
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
        windows: List<Window<UmlDiagram>>
    ): List<EObject> {
        TODO("Not yet implemented")
    }

    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        TODO("Not yet implemented")
    }

    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    override fun getDisplayContent(): DisplayContentMapper<UmlDiagram> {
        TODO("Not yet implemented")
    }

}