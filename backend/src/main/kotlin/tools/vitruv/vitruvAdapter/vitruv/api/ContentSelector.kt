package tools.vitruv.vitruvAdapter.vitruv.api

import tools.vitruv.framework.views.ViewSelector

/**
 * This interface represents a content selector, which can be used to select content in the Vitruvius graphical editor,
 * but only for the given windows.
 */
interface ContentSelector {

    /**
     * Applies a selection to viewSelector.selectableElements.
     * This method should only select the content for the given windows.
     * @param viewSelector the viewSelector to select the elements within
     * @param windows the windows to select the content for
     * @return The viewSelector with the selected elements.
     */
    fun applySelection(viewSelector: ViewSelector, windows: Set<String>)

}