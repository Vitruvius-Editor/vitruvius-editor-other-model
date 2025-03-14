package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Represents the type of a connection between two UML nodes.
 * @param uuid The unique identifier of the connection type.
 */
enum class UmlConnectionType(
    @JsonValue
    val uuid: String,
) {
    /**
     * Represents a generalization connection.
     */
    EXTENDS("extends"),

    /**
     * Represents an interface implementation connection.
     */
    IMPLEMENTS("implements"),

    /**
     * Represents an association connection.
     */
    ASSOCIATION("association"),

    /**
     * Represents an import connection.
     */
    IMPORT("import"),
}
