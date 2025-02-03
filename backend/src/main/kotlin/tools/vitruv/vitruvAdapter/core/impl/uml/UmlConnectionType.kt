package tools.vitruv.vitruvAdapter.core.impl.uml

enum class UmlConnectionType (val uuid: String){
    EXTENDS("extends"),
    IMPLEMENTS("implements"),
    ASSOCIATION("association"),
    IMPORT("import"),

}