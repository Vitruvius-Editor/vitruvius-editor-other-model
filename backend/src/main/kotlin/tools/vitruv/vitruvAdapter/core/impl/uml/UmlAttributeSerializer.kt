package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class UmlAttributeSerializer: JsonSerializer<UmlAttribute>() {
    override fun serialize(value: UmlAttribute?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        var serialized = ""
        if (value != null) {
            serialized = getSerializedString(value)
        }
        gen?.writeString(serialized)
    }

    fun getSerializedString(value: UmlAttribute): String {
        return "${value.visibility.symbol} ${value.name}: ${value.type}"
    }
}