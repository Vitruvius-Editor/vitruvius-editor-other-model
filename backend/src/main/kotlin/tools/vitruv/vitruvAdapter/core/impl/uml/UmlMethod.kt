package tools.vitruv.vitruvAdapter.core.impl.uml


/**
 * Represents a method of a UML class.
 * @param uuid The unique identifier of the method.
 * @param visibility The visibility of the method.
 * @param name The name of the method.
 * @param parameters The parameters of the method.
 * @param returnType The return type of the method.
 * @see UmlVisibility
 */
data class UmlMethod (
    val uuid: String,
    val visibility: UmlVisibility,
    val name: String,
    val parameters: List<UmlParameter>,
    val returnType: UmlType
)