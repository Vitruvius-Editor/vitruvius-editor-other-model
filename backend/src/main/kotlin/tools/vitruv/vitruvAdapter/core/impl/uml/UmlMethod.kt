package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = UmlMethodSerializer::class)
@JsonDeserialize(using = UmlMethodDeserializer::class)
data class UmlMethod (
    val visibility: UmlVisibility,
    val name: String,
    val parameters: List<UmlParameter>,
    val returnType: String
)