package tools.vitruv.vitruvAdapter.vitruv.api

/**
 * A container for display views.
 * @see DisplayView
 * @author uhsab
 */
interface DisplayViewContainer {

    /**
     * Registers a display view to the adapter.
     * @param displayView The display view to register.
     * @throws IllegalArgumentException If a display view with the same name is already registered.
     */
    fun registerDisplayView(displayView: DisplayView)

    /**
     * Registers a list of display views to the adapter.
     * @param displayViews The display views to register.
     * @throws IllegalArgumentException If a display view with the same name is already registered.
     */
    fun registerDisplayViews(displayViews: Set<DisplayView>)

    /**
     * Gets all registered display views.
     * @return The registered display views.
     */
    fun getDisplayViews(): Set<DisplayView>

    /**
     * Gets a display view by its name.
     * @param name The name of the display view.
     * @return The display view with the given name or null if no display view with the given name is registered.
     */
    fun getDisplayView(name: String): DisplayView?
}