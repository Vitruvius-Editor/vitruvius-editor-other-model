package tools.vitruv.vitruvAdapter.logic.api.displayViews

import tools.vitruv.vitruvAdapter.logic.api.contents.Content

/**
 * Interface for display views that can be modified.
 * @author uhsab
 */
interface ModifiableDisplayView : DisplayView{
    fun updateViewContent(newContent: Content)
}