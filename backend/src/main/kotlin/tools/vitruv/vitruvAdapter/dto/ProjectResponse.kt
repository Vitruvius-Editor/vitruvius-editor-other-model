package tools.vitruv.vitruvAdapter.dto

import java.util.UUID

/**
 * This data class holds all properties that are returned when requesting project data.
 *
 * @property name The name of the project.
 * @property description The description of the project.
 * @property uuid The uuid of the project.
 * @property url The location of the project.
 */
data class ProjectResponse(val name: String, val description: String, val uuid: UUID, val url: String) {
}