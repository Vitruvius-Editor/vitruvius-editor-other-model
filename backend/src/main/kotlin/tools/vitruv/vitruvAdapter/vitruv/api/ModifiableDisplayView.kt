package tools.vitruv.vitruvAdapter.vitruv.api

/**
 * Interface for display views that can be modified.
 * @author uhsab
 */
interface ModifiableDisplayView : DisplayView {
    fun updateViewContent(newDisplayContent: DisplayContent)
}