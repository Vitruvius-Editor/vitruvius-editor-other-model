package tools.vitruv.vitruvAdapter.core.api

import org.eclipse.emf.ecore.EObject
import tools.vitruv.framework.views.View

/**
 * This interface represents a content selector, which selects the Elements that are contained in the given windows.
 */
interface ContentSelector {

    /**
     * Applies a selection to viewSelector.selectableElements.
     * This method should only select the content for the given windows.
     * @param viewSelector the viewSelector to select the elements within
     * @param windows the windows to select the content for
     * @return The viewSelector with the selected elements.
     */
    fun applySelection(view: View, windows: Set<String>) : List<EObject>
}