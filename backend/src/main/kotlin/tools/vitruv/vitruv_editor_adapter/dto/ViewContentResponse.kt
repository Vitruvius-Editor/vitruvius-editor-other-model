package tools.vitruv.vitruv_editor_adapter.dto

/**
 * This data class holds all properties that are returned when requesting the content of a view.
 *
 * @property name The name of the view.
 * @property description The description of the view.
 * @property id The id of the view.
 * @property content The content of the view.
 */
data class ViewContentResponse(val name: String, val description: String, val id: String, val content: String) {
}