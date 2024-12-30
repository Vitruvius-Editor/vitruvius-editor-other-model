package tools.vitruv.vitruvAdapter.vitruv.api

import tools.vitruv.framework.views.View
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.framework.views.changederivation.DefaultStateBasedChangeResolutionStrategy

/**
 * This interface represents a display view, which can be displayed in the Vitruvius graphical editor.
 * @author uhsab
 */
abstract class DisplayView {

    /**
     * Gets the name of the DisplayView.
     * @return name
     */
    abstract fun getName(): String

    /**
     * Gets the relating vitruvius view type from the server.
     * @return The relating vitruvius view type.
     */
    abstract fun getViewType(): ViewType<out ViewSelector>

    /**
     * Gets the ViewMapper for the DisplayView
     * @return the ViewMapper.
     */
    abstract fun getViewMapper(): ViewMapper

    /**
     * Gets the WindowSelector for the DisplayView
     * @return the WindowSelector
     */
    abstract fun getWindowSelector(): Selector

    /**
     * Gets the WindowSelector for the DisplayView
     * @return the WindowSelector
     */
    abstract fun getContentSelector(): Selector

    /**
     * Gets all windows that are available for this view.
     * @see Window
     * @return The windows that are available for this view.
     */
    fun getWindows(): Set<String> {
        val internalSelector = getViewType().createSelector(null)
        getWindowSelector().applySelection(internalSelector)
        //Now only the names of the windows should be in the selection
        val view = internalSelector.createView()
        val windows = mutableSetOf<String>()
        for (rootObject in view.rootObjects) {
            //Add all names of windows to windows set
        }
        return windows
    }

    private fun getViewForWindows(windows: Set<String>): View {
        val internalSelector = getViewType().createSelector(null)
        getContentSelector().applySelection(internalSelector)
        //Now only the things needed to create content for the windows should be in the selection
        return internalSelector.createView()
    }

    /**
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created content for each window.
     */
    fun createWindowContent(windows: Set<String>): DisplayContent {
        return getViewMapper().mapViewToJson(getViewForWindows(windows).rootObjects.toList())
    }

    /**
     * This method reverts the json that Theia can interpret to display views to EObjects and tries to
     * apply the changes to the model by state changed derivation strategy.
     * @param json the json String
     */
    fun editDisplayView(json: String) {
        val newViewContent = getViewMapper().mapJsonToView(json)
        val oldViewContent = getViewForWindows(getWindows())
        val strategy = DefaultStateBasedChangeResolutionStrategy().getChangeSequenceBetween(old, new) //How to convert to ressource?
        //Update
    }

}
