package tools.vitruv.vitruvAdapter.core.impl.uml

/**
 * Represents a UML diagram.
 * @param nodes The nodes of the diagram.
 * @param connections The connections between the nodes.
 */
data class UmlDiagram(
    val nodes: List<UmlNode>,
    val connections: List<UmlConnection>,
)
