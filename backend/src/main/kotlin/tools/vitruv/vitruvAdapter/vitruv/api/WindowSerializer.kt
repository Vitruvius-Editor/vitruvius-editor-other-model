package tools.vitruv.vitruvAdapter.vitruv.api

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class WindowSerializer: JsonSerializer<Window>() {
    override fun serialize(window: Window, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("windowName", window.getWindowName())
        gen.writeStringField("content", window.getContent())
        gen.writeEndObject()
    }

}