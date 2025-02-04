package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException

class UmlAttributeDeserializer: JsonDeserializer<UmlAttribute>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UmlAttribute {
        // Get the string value (e.g., "+ attribute1: String")
        val text = p.valueAsString.trim()
        if (text.isEmpty()) {
            throw JsonMappingException(p, "Empty attribute string")
        }

        // Example format: "<visibility> <name>: <type>"
        // For instance: "+ attribute1: String"

        // Extract the visibility symbol (first character)
        val visibilitySymbol = text.first().toString()

        // Remove the symbol and any following whitespace
        val rest = text.drop(1).trimStart()

        // Split into name and type using the colon as delimiter
        val colonIndex = rest.indexOf(':')
        if (colonIndex < 0) {
            throw JsonMappingException(p, "Invalid attribute format, missing ':' in '$text'")
        }

        val namePart = rest.substring(0, colonIndex).trim()
        val typePart = rest.substring(colonIndex + 1).trim()

        // Convert the visibility symbol to an UmlVisibility
        // (Assuming you have a method like UmlVisibility.fromSymbol)
        val visibility = UmlVisibility.fromSymbol(visibilitySymbol)
            ?: throw JsonMappingException(p, "Invalid visibility symbol: $visibilitySymbol")

        // Create and return the UmlAttribute instance
        return UmlAttribute(visibility, namePart, typePart)
    }
}