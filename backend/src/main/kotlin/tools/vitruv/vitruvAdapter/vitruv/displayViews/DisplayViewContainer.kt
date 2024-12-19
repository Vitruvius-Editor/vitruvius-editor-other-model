package tools.vitruv.vitruvAdapter.vitruv.displayViews

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
    fun registerDisplayView(displayView: tools.vitruv.vitruvAdapter.vitruv.displayViews.DisplayView)

    /**
     * Registers a list of display views to the adapter.
     * @param displayViews The display views to register.
     * @throws IllegalArgumentException If a display view with the same name is already registered.
     */
    fun registerDisplayViews(displayViews: Set<tools.vitruv.vitruvAdapter.vitruv.displayViews.DisplayView>)

    /**
     * Gets all registered display views.
     * @return The registered display views.
     */
    fun getDisplayViews(): Set<tools.vitruv.vitruvAdapter.vitruv.displayViews.DisplayView>
}