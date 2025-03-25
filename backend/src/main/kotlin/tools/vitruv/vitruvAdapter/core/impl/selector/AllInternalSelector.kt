package tools.vitruv.vitruvAdapter.core.impl.selector

import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.vitruvAdapter.core.api.InternalSelector

/**
 * A selector that selects all selectable elements within a viewSelector.
 *
 */
class AllInternalSelector : InternalSelector {
    override fun applySelection(viewSelector: ViewSelector) {
        viewSelector.selectableElements.forEach { viewSelector.setSelected(it, true) }
    }
}
