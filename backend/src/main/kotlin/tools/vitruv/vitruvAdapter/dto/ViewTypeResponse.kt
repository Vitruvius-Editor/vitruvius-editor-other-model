package tools.vitruv.vitruvAdapter.dto

/**
 * This data class holds all properties about a view type.
 *
 * @property name The name of the view type.
 * @property views A list of all views of the view type.
 */
data class ViewTypeResponse(
    val name: String,
    val views: List<String>
)