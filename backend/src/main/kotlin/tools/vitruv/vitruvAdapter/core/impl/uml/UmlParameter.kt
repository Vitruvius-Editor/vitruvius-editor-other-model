package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = UmlParameterSerializer::class)
@JsonDeserialize(using = UmlParameterDeserializer::class)
class UmlParameter (
    val name: String,
    val type: String
)