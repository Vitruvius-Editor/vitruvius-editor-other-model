package tools.vitruv.vitruvAdapter.core.impl.uml

enum class UmlVisibility (val symbol: String) {
    PUBLIC("+"),
    PROTECTED("#"),
    PRIVATE("-"),
    PACKAGE("~")
}