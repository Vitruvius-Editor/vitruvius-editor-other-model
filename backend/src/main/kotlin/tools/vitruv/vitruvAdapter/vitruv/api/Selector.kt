package tools.vitruv.vitruvAdapter.vitruv.api

import tools.vitruv.framework.views.ViewSelector

/**
 * A Selector is an interface which selects elements of a ViewSelector.
 */
interface Selector {
    /**
     * Applies a selection to viewSelector.selectableElements.
     * @param viewSelector the viewSelector to select the elements within
     */
    fun applySelection(viewSelector: ViewSelector)
}