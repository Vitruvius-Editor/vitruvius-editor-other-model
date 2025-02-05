package tools.vitruv.vitruvAdapter.dto

import tools.vitruv.vitruvAdapter.model.ConnectionDetails
import java.util.UUID

/**
 * This data class holds all properties that are returned when requesting project data.
 *
 * @property name The name of the project.
 * @property description The description of the project.
 * @property uuid The uuid of the project.
 * @property url The location of the project.
 */
data class ConnectionResponse(
    val name: String,
    val description: String,
    val uuid: UUID?,
    val url: String,
    val port: Int,
)
{
    constructor(connectionDetails: ConnectionDetails): this(connectionDetails.name, connectionDetails.description, connectionDetails.uuid, connectionDetails.url, connectionDetails.port)
}
