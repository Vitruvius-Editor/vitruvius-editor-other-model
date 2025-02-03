package tools.vitruv.vitruvAdapter.core.impl.uml

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