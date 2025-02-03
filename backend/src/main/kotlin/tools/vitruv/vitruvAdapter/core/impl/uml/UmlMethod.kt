package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = UmlMethodSerializer::class)
data class UmlMethod (
    val visibility: UmlVisibility,
    val name: String,
    val parameters: List<UmlAttribute>,
    val returnType: String
)