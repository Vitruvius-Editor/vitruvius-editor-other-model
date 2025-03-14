package tools.vitruv.vitruvAdapter.core.impl.uml

/**
 * Represents an attribute of a UML class.
 * @param uuid The unique identifier of the attribute.
 * @param visibility The visibility of the attribute.
 * @param name The name of the attribute.
 */
data class UmlAttribute(
    val uuid: String,
    val visibility: UmlVisibility,
    val name: String,
    val type: UmlType,
)
