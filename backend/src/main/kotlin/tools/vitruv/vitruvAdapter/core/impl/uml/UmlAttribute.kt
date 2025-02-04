package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = UmlAttributeSerializer::class)
@JsonDeserialize(using = UmlAttributeDeserializer::class)
data class UmlAttribute(
    val visibility: UmlVisibility,
    val name: String,
    val type: String
)