package tools.vitruv.vitruvAdapter.vitruv.impl.selector

import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.vitruvAdapter.vitruv.api.Selector

/**
 * A selector that selects all given elements.
 *
 */
class AllSelector : Selector {
    override fun applySelection(viewSelector: ViewSelector) {
        viewSelector.selectableElements.forEach{ viewSelector.setSelected(it, true)}
    }
}
