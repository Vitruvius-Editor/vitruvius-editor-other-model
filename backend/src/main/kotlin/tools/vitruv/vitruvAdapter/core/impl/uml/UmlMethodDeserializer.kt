package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException

class UmlMethodDeserializer: JsonDeserializer<UmlMethod>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UmlMethod {
        val umlParameterDeserializer = UmlParameterDeserializer()
        // Get the full method string (e.g. "+ myMethod(param1: String, param2: Int):Boolean")
        val text = p.valueAsString.trim()
        if (text.isEmpty()) {
            throw JsonMappingException(p, "Empty method string")
        }

        // 1. Extract the visibility symbol (first character)
        val visibilitySymbol = text.first().toString()
        val visibility = UmlVisibility.fromSymbol(visibilitySymbol)
            ?: throw JsonMappingException(p, "Invalid visibility symbol: $visibilitySymbol")

        // 2. Remove the visibility symbol and any following whitespace.
        val afterVisibility = text.drop(1).trimStart()

        // 3. Find the opening parenthesis '(' to determine the end of the method name.
        val parenStartIndex = afterVisibility.indexOf('(')
        if (parenStartIndex < 0) {
            throw JsonMappingException(p, "Invalid method format: missing '(' in '$text'")
        }
        val methodName = afterVisibility.substring(0, parenStartIndex).trim()

        // 4. Find the closing parenthesis ')' for the parameter list.
        val parenEndIndex = afterVisibility.indexOf(')', parenStartIndex)
        if (parenEndIndex < 0) {
            throw JsonMappingException(p, "Invalid method format: missing ')' in '$text'")
        }

        // 5. Extract the parameter list substring (inside the parentheses).
        val parametersString = afterVisibility.substring(parenStartIndex + 1, parenEndIndex).trim()

        // 6. Look for the colon ':' following the parameters to separate the return type.
        val colonIndex = afterVisibility.indexOf(':', parenEndIndex)
        if (colonIndex < 0) {
            throw JsonMappingException(p, "Invalid method format: missing ':' after parameters in '$text'")
        }
        val returnType = afterVisibility.substring(colonIndex + 1).trim()

        // 7. Parse the parameters. They are assumed to be comma-separated.
        val parameters = if (parametersString.isEmpty()) {
            emptyList()
        } else {
            parametersString.split(",")
                .map { paramText -> umlParameterDeserializer.getDeserializedParameter(paramText) }
        }

        return UmlMethod(visibility, methodName, parameters, returnType)
    }

}