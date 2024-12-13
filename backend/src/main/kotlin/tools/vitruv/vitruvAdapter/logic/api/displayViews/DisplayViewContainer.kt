package tools.vitruv.vitruvAdapter.logic.api.displayViews

/**
 * A container for display views.
 * @see DisplayView
 * @author uhsab
 */
interface DisplayViewContainer {

    /**
     * Registers a display view to the adapter.
     * @param displayView The display view to register.
     */
    fun registerDisplayView(displayView: DisplayView)

    /**
     * Registers a list of display views to the adapter.
     * @param displayViews The display views to register.
     */
    fun registerDisplayViews(displayViews: Set<DisplayView>)

    /**
     * Gets all registered display views.
     * @return The registered display views.
     */
    fun getDisplayViews(): Set<DisplayView>
}