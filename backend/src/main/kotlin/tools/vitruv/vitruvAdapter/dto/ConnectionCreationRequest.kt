package tools.vitruv.vitruvAdapter.dto

/**
 * Data class that holds all properties required to create a new project.
 *
 * @property name The name of the project.
 * @property description The description of the project.
 * @property url The location of the project.
 */
data class ConnectionCreationRequest(val name: String, val description: String, val url: String) {
}
/**
 * Type alias, because editing a project requires the same properties as creating.
 */
typealias ConnectionEditRequest = ConnectionCreationRequest