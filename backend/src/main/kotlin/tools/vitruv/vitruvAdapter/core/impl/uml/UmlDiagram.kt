package tools.vitruv.vitruvAdapter.core.impl.uml

/**
 * Represents a UML diagram.
 */
data class UmlDiagram (
    val nodes: List<UmlNode>,
    val connections : List<UmlConnection>
){
}