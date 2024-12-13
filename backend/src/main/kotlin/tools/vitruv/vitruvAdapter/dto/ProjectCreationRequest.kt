package tools.vitruv.vitruvAdapter.dto

/**
 * Data class that holds all properties required to create a new project.
 *
 * @property name The projects name.
 * @property description The projects' description.
 * @property url The projects' location.
 */
data class ProjectCreationRequest(val name: String, val description: String, val url: String) {
}
/**
 * Type alias, because editing a project requires the same properties as creating.
 */
typealias ProjectEditRequest = ProjectCreationRequest