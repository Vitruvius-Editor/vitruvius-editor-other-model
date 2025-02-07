package tools.vitruv.vitruvAdapter.core.impl.uml

/**
 * Represents the visibility of a UML element.
 * @param symbol The symbol representing the visibility.
 * @see UmlAttribute
 */
enum class UmlVisibility (val symbol: String) {
    PUBLIC("+"),
    PROTECTED("#"),
    PRIVATE("-"),
    PACKAGE("~"), ;

    companion object {
        fun fromSymbol(visibilitySymbol: String): UmlVisibility? {
            return entries.find { it.symbol == visibilitySymbol }
        }
    }
}