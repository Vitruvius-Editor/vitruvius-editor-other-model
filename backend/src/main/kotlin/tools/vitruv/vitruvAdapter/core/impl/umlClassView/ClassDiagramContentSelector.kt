package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.core.api.ContentSelector

/**
 * Selects the content of a class diagram.
 */
class ClassDiagramContentSelector: ContentSelector{
    /**
     * Selects the view elements that are needed for the windows.
     * @param rootObjects the root objects of the view
     * @param windows the elements to select
     * @return the selected elements
     */
    override fun applySelection(rootObjects: List<EObject>, windows: Set<String>): List<EObject> {
        TODO("Select all UML classes in the given windows packages and return them")
    }
}