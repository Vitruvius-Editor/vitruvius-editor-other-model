package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class UmlParameterSerializer: JsonSerializer<UmlParameter>() {

    override fun serialize(value: UmlParameter?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        var serialized = ""
        if (value != null) {
            serialized = getSerializedString(value)
        }
        gen?.writeString(serialized)
    }

    fun getSerializedString(value: UmlParameter): String {
        return "${value.name}: ${value.type}"
    }
}