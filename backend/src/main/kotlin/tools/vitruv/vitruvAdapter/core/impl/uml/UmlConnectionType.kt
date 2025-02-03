package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.annotation.JsonValue


enum class UmlConnectionType (
    @JsonValue
    val uuid: String
){
    EXTENDS("extends"),
    IMPLEMENTS("implements"),
    ASSOCIATION("association"),
    IMPORT("import");

}