package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider


class UmlMethodSerializer: JsonSerializer<UmlMethod>() {
    override fun serialize(value: UmlMethod?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        var serialized = ""
        val umlAttributeSerializer = UmlAttributeSerializer()
        if (value != null) {
            serialized =
                "${value.visibility.symbol} ${value.name}" +
                        "(${value.parameters.map { umlAttributeSerializer.getSerializedString(it) }}):" +
                        value.returnType
        }
        gen?.writeString(serialized)
    }
}