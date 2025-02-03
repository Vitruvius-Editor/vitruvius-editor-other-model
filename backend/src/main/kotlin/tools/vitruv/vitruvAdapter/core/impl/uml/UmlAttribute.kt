package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = UmlAttributeSerializer::class)
data class UmlAttribute (
    val visibility: UmlVisibility,
    val name: String,
    val type: String
)