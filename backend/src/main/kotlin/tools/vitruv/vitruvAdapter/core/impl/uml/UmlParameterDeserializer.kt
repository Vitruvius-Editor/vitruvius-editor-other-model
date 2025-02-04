package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException

class UmlParameterDeserializer: JsonDeserializer<UmlParameter>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UmlParameter {
        // Get the string (e.g. "param1: String")
        val text = p.valueAsString.trim()
        if (text.isEmpty()) {
            throw JsonMappingException(p, "Empty parameter string")
        }

        // Find the colon that separates the name and type.
        val colonIndex = text.indexOf(':')
        if (colonIndex < 0) {
            throw JsonMappingException(p, "Invalid parameter format, missing ':' in '$text'")
        }

        // Extract the name and type, trimming any whitespace.
        val name = text.substring(0, colonIndex).trim()
        val type = text.substring(colonIndex + 1).trim()

        return UmlParameter(name, type)
    }

    fun getDeserializedParameter(text: String): UmlParameter {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            throw IllegalArgumentException("Empty parameter definition")
        }

        val colonIndex = trimmed.indexOf(':')
        if (colonIndex < 0) {
            throw IllegalArgumentException("Invalid parameter format, missing ':' in '$text'")
        }

        val name = trimmed.substring(0, colonIndex).trim()
        val type = trimmed.substring(colonIndex + 1).trim()

        return UmlParameter(name, type)
    }
}