package tools.vitruv.vitruvAdapter.vitruv.api

import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType

/**
 * This interface represents a display view, which can be displayed in the Vitruvius graphical editor.
 * @author uhsab
 */
abstract class DisplayView(
    val name: String,
    protected val viewMapper: ViewMapper,
    protected val windowSelector: Selector,
    protected val contentSelector: Selector
) {

    /**
     * Gets the relating vitruvius view type from the server.
     * @return The relating vitruvius view type.
     */
    abstract fun getViewType(): ViewType<out ViewSelector>

    /**
     * Gets all windows that are available for this view.
     * @see Window
     * @param selector The selector to apply. To select all use the AllSelector
     * @return The windows that are available for this view.
     */
    fun getWindows(selector: Selector): Set<String> {
        val internalSelector = getViewType().createSelector(null)
        windowSelector.applySelection(internalSelector)
        //Now only the names of the windows should be in the selection
        val view = internalSelector.createView()
        val windows = mutableSetOf<String>()
        for (rootObject in view.rootObjects) {
            //Add all names of windows to windows set
        }
        return windows
    }

    /**
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created content for each window.
     */
    fun createWindowContent(windows: Set<String>): DisplayContent {
        val internalSelector = getViewType().createSelector(null)
        contentSelector.applySelection(internalSelector)
        //Now only the things needed to create content for the windows should be in the selection
        val view = internalSelector.createView()
        return viewMapper.mapViewToJson(view.rootObjects.toList())
    }

}
