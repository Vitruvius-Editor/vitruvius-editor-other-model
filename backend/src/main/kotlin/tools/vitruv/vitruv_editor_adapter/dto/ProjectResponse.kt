package tools.vitruv.vitruv_editor_adapter.dto

/**
 * This data class holds all properties that are returned when requesting project data.
 *
 * @property name The name of the project.
 * @property description The description of the project.
 * @property id The id of the project.
 * @property location The location of the project.
 */
data class ProjectResponse(val name: String, val description: String, val id: String, val location: String) {
}