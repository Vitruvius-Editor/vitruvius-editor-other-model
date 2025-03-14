package tools.vitruv.vitruvAdapter.core.api.testutils

import com.fasterxml.jackson.databind.ObjectMapper

class JsonNormalizer private constructor() {
    companion object {
        fun normalize(json: String): String {
            val objectMapper = ObjectMapper()
            // Normalize both JSON strings to remove formatting differences
            val normalizedExpectedJson =
                objectMapper.writeValueAsString(
                    objectMapper.readTree(json),
                )
            return normalizedExpectedJson
        }
    }
}
