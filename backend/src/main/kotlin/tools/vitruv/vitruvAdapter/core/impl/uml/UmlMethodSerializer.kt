package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider


class UmlMethodSerializer: JsonSerializer<UmlMethod>() {
    override fun serialize(value: UmlMethod?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        var serialized = ""
        val umlParameterSerializer = UmlParameterSerializer()
        if (value != null) {
            val serializedParameters = value.parameters.joinToString(", ") {
                umlParameterSerializer.getSerializedString(it)
            }
            serialized = "${value.visibility.symbol} ${value.name}($serializedParameters):${value.returnType}"
        }
        gen?.writeString(serialized)
    }
}