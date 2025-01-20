package tools.vitruv.vitruvAdapter.vitruv.api

import org.springframework.stereotype.Service
import tools.vitruv.framework.views.View
import tools.vitruv.framework.views.changederivation.DefaultStateBasedChangeResolutionStrategy
import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.remote.client.exception.BadClientResponseException
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.exception.VitruviusConnectFailedException
import tools.vitruv.vitruvAdapter.exception.DisplayViewException

/**
 * This class is the adapter for the Vitruvius model server. It provides methods to interact with the model server.
 * This class contains the methods for the frontend to interact with the model server.
 */
@Service
class VitruvAdapter {

    private var vitruvClient: VitruvClient? = null
    private var displayViewContainer: DisplayViewContainer? = null


    /**
     * Connects the adapter to the model server.
     * @param vitruvClient The client to connect to the model server.
     */
    fun connectClient(vitruvClient: VitruvClient) {
        try {
            vitruvClient.viewTypes
        } catch (e: BadClientResponseException) {
            throw VitruviusConnectFailedException("Could not connect to model server.")
        }
        this.vitruvClient = vitruvClient
    }

    /**
     * Sets the DisplayViewContainer for the adapter.
     * @param displayViewContainer The DisplayViewContainer to set.
     */
    fun setDisplayViewContainer(displayViewContainer: DisplayViewContainer) {
        this.displayViewContainer = displayViewContainer
    }


    /**
     * Returns all available DisplayViews.
     * @return The available DisplayViews.
     */
    fun getDisplayViews(): Set<DisplayView> = displayViewContainer?.getDisplayViews() ?: emptySet()
    fun getDisplayView(name: String): DisplayView? = displayViewContainer?.getDisplayView(name)

    /**
     * Gets all windows that are available for a given DisplayView.
     * @param displayView The DisplayView to get the windows for.
     * @return The windows that are available for the given DisplayView.
     */
    fun getWindows(displayView : DisplayView): Set<String> {
        val internalSelector = getViewType(displayView).createSelector(null)
        displayView.windowSelector.applySelection(internalSelector)
        return displayView.viewMapper.mapViewToWindows(internalSelector.createView().rootObjects.toList())
    }

    /**
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created View for the windows.
     */
    private fun getViewForWindows(displayView: DisplayView, windows: Set<String>): View {
        val internalSelector = getViewType(displayView).createSelector(null)
        displayView.contentSelector.applySelection(internalSelector)
        // Now only the things needed to create content for the windows should be in the selection
        return internalSelector.createView()
        TODO("Select all windows")
    }

    /**
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created content for each window.
     */
    fun createWindowContent(displayView: DisplayView, windows: Set<String>): String {
        val view = getViewForWindows(displayView, windows)
        val mapper = displayView.viewMapper
        val viewInformation = JsonViewInformation(mapper.getDisplayContent())
        val mappedData = mapper.mapViewToContentData(view.rootObjects.toList())
        val json = viewInformation.parseWindowsToJson(mappedData)
        return json
    }


    /**
     * This method reverts the json that Theia can interpret to display views to EObjects and tries to
     * apply the changes to the model by state changed derivation strategy.
     * @param displayView The DisplayView to edit.
     * @param json The json to edit the DisplayView with.
     */
    fun editDisplayView(displayView: DisplayView, json: String) {
        val mapper = displayView.viewMapper
        val viewInformation = JsonViewInformation(mapper.getDisplayContent())
        val retrievedEObjects = mapper.mapContentDataToView(viewInformation.parseWindowsFromJson(json))
        val oldViewContent = getViewForWindows(displayView, getWindows(displayView))
        val view = oldViewContent.withChangeDerivingTrait(DefaultStateBasedChangeResolutionStrategy())
        view.rootObjects.clear()
        view.rootObjects.addAll(retrievedEObjects)
        view.commitChanges()
    }

    /**
     * Collects the windows from the given json string.
     * @param displayView The DisplayView to collect the windows for.
     * @param json The json string to collect the windows from.
     * @return The collected windows.
     */
    fun collectWindowsFromJson(displayView: DisplayView, json: String): List<String> {
        val mapper = displayView.viewMapper
        val viewInformation = JsonViewInformation(mapper.getDisplayContent())
        return viewInformation.collectWindowsFromJson(json)
    }


    private fun getViewType(displayView: DisplayView): ViewType<out ViewSelector> {
        val client = vitruvClient ?: throw IllegalStateException("No client connected.")
        val viewType = client.viewTypes.find { it.name == displayView.viewTypeName }
        if (viewType == null ) {
            throw DisplayViewException("View type ${displayView.viewTypeName} not found on model server.")
        }
        return viewType
    }
}
