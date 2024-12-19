package tools.vitruv.vitruvAdapter.vitruv.displayViews

import tools.vitruv.vitruvAdapter.vitruv.contents.Content

/**
 * Interface for display views that can be modified.
 * @author uhsab
 */
interface ModifiableDisplayView : tools.vitruv.vitruvAdapter.vitruv.displayViews.DisplayView {
    fun updateViewContent(newContent: tools.vitruv.vitruvAdapter.vitruv.contents.Content)
}