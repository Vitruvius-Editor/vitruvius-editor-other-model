package tools.vitruv.vitruvAdapter.vitruv.api

import org.springframework.stereotype.Service
import tools.vitruv.framework.views.View

@Service
class VitruvAdapter {
    fun getWindows(displayView : DisplayView): Set<String> {
        val internalSelector = displayView.viewType.createSelector(null)
        displayView.windowSelector.applySelection(internalSelector)
        // Now only the names of the windows should be in the selection
        val view = internalSelector.createView()
        val windows = mutableSetOf<String>()
        for (rootObject in view.rootObjects) {
            // Add all names of windows to windows set
        }
        return windows
    }


    /**
     * Gets all windows that are available for this view.
     * @see Window
     * @return The windows that are available for this view.
     */


    private fun getViewForWindows(displayView: DisplayView, windows: Set<String>): View {
        val internalSelector = displayView.viewType.createSelector(null)
        displayView.contentSelector.applySelection(internalSelector)
        // Now only the things needed to create content for the windows should be in the selection
        return internalSelector.createView()
    }

    /**
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created content for each window.
     */
    fun createWindowContent(displayView: DisplayView, windows: Set<String>): DisplayContent =
        displayView.viewMapper.mapViewToJson(getViewForWindows(displayView, windows).rootObjects.toList())

    /**
     * This method reverts the json that Theia can interpret to display views to EObjects and tries to
     * apply the changes to the model by state changed derivation strategy.
     * @param json the json String
     */
    fun editDisplayView(displayView: DisplayView, json: String) {
        val newViewContent = displayView.viewMapper.mapJsonToView(json)
        val oldViewContent = getViewForWindows(displayView, getWindows(displayView))
        // val strategy = DefaultStateBasedChangeResolutionStrategy().getChangeSequenceBetween(old, new) //How to convert to ressource?
        // Update
    }
}