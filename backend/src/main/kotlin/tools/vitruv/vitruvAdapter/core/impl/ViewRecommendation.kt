package tools.vitruv.vitruvAdapter.core.impl

/**
 * This class represents a recommendation for a view.
 * It can be sent to the frontend to recommend a view to the user, which can then be manually loaded.
 * @param displayViewName The name of the display view.
 * @param windowName The name of the window.
 * @author uhsab
 */
data class ViewRecommendation(val displayViewName: String, val windowName: String)