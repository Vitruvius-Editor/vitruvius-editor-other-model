package tools.vitruv.vitruvAdapter.vitruv.api

import org.eclipse.emf.ecore.EObject
import tools.vitruv.framework.views.View
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import tools.vitruv.framework.views.changederivation.DefaultStateBasedChangeResolutionStrategy

class VitruvAdapter {


    /**
     * Gets all windows that are available for this view.
     * @see Window
     * @return The windows that are available for this view.
     */
    fun getWindows(displayView: DisplayView): Set<String> {
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
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created content for each window.
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
     * @param displayView the display view
     */
    fun editDisplayView(displayView: DisplayView, json: String) {
        val newViewContent = displayView.viewMapper.mapJsonToView(json)
        val oldViewContent = getViewForWindows(displayView, getWindows(displayView))
        val newViewResource = convertEObjectsToResource(newViewContent)
        val oldViewResource = convertViewToResource(oldViewContent)
        val strategy = DefaultStateBasedChangeResolutionStrategy().getChangeSequenceBetween(newViewResource, oldViewResource)
        // apply change to model

    }

    private fun convertEObjectsToResource(eObjects: List<EObject>): Resource? {
        // TODO: Implement resource creation, add contents, and optionally save
        return null
    }
    private fun convertViewToResource(view: View): Resource? {
        // TODO: Implement resource creation, add contents, and optionally save
        return null
    }


}