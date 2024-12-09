package tools.vitruv.vitruvAdapter.dto

/**
 * This data class holds all metadata provided by a view.
 *
 * @property name The name of the view.
 * @property description The description of the view.
 * @property id The id of the view.
 * @property editable Whether the view is editable.
 */
data class ViewResponse(val name: String, val description: String, val id: String, val editable: Boolean) {
}