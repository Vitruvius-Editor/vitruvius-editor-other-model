package tools.vitruv.vitruvAdapter.vitruv.api

import tools.vitruv.framework.views.View
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType

/**
 * This interface represents a display view, which can be displayed in the Vitruvius graphical editor.
 * @author uhsab
 */
interface DisplayView {
    val name: String
    val viewType: ViewType<out ViewSelector>
    val viewMapper: ViewMapper
    val windowSelector: Selector
    val contentSelector: Selector

    /**
     * Gets all windows that are available for this view.
     * @see Window
     * @return The windows that are available for this view.
     */
    fun getWindows(): Set<String> {
        val internalSelector = viewType.createSelector(null)
        windowSelector.applySelection(internalSelector)
        // Now only the names of the windows should be in the selection
        val view = internalSelector.createView()
        val windows = mutableSetOf<String>()
        for (rootObject in view.rootObjects) {
            // Add all names of windows to windows set
        }
        return windows
    }

    private fun getViewForWindows(windows: Set<String>): View {
        val internalSelector = viewType.createSelector(null)
        contentSelector.applySelection(internalSelector)
        // Now only the things needed to create content for the windows should be in the selection
        return internalSelector.createView()
    }

    /**
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created content for each window.
     */
    fun createWindowContent(windows: Set<String>): DisplayContent =
        viewMapper.mapViewToJson(getViewForWindows(windows).rootObjects.toList())

    /**
     * This method reverts the json that Theia can interpret to display views to EObjects and tries to
     * apply the changes to the model by state changed derivation strategy.
     * @param json the json String
     */
    fun editDisplayView(json: String) {
        val newViewContent = viewMapper.mapJsonToView(json)
        val oldViewContent = getViewForWindows(getWindows())
        // val strategy = DefaultStateBasedChangeResolutionStrategy().getChangeSequenceBetween(old, new) //How to convert to ressource?
        // Update
    }
}
