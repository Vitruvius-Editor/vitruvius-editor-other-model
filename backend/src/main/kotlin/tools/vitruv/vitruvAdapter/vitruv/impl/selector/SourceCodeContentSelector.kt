package tools.vitruv.vitruvAdapter.vitruv.impl.selector

import org.eclipse.uml2.uml.Class
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.vitruvAdapter.vitruv.api.ContentSelector

class SourceCodeContentSelector: ContentSelector {
    /**
     * Applies a selection to viewSelector.selectableElements.
     * This method should only select the content for the given windows.
     * @param viewSelector the viewSelector to select the elements within
     * @param windows the windows to select the content for
     * @return The viewSelector with the selected elements.
     */
    override fun applySelection(viewSelector: ViewSelector, windows: Set<String>) {
        for (window in windows) {
            val elements = viewSelector.selectableElements
            for (element in elements) {
                if (element !is Class) {
                    continue
                }
                val clazz = element as Class
                if (clazz.name == window) {
                    viewSelector.setSelected(clazz, true)
                }
            }
        }
    }


}