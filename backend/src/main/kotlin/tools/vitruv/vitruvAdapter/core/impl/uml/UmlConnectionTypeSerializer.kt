package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class UmlConnectionTypeSerializer: JsonSerializer<UmlConnectionType>() {
    override fun serialize(value: UmlConnectionType?, gen: JsonGenerator, serializers: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
        } else {
            // Write only the uuid value as a JSON string.
            gen.writeString(value.uuid)
        }
    }
}