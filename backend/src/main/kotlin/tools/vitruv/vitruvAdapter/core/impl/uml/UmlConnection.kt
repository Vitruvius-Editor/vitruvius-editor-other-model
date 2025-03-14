package tools.vitruv.vitruvAdapter.core.impl.uml

/**
 * Represents a connection between two UML nodes.
 * @param uuid The unique identifier of the connection.
 * @param sourceNodeUUID The unique identifier of the source node.
 * @param targetNodeUUID The unique identifier of the target node.
 * @param connectionType The type of the connection.
 * @param sourceMultiplicity The multiplicity of the source node.
 * @param targetMultiplicity The multiplicity of the target node.
 * @param connectionName The name of the connection.
 * @see UmlConnectionType
 */
data class UmlConnection(
    val uuid: String,
    val sourceNodeUUID: String,
    val targetNodeUUID: String,
    val connectionType: UmlConnectionType,
    val sourceMultiplicity: String,
    val targetMultiplicity: String,
    val connectionName: String,
)
