package tools.vitruv.vitruvAdapter.core.impl.uml

/**
 * Represents a parameter of a UML method.
 * @param uuid The unique identifier of the parameter.
 * @param name The name of the parameter.
 * @param type The type of the parameter.
 */
data class UmlParameter(
    val uuid: String,
    val name: String,
    val type: UmlType,
)
