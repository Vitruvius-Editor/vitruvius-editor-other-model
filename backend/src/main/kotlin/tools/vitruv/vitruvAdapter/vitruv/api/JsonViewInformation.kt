package tools.vitruv.vitruvAdapter.vitruv.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonSerialize

class JsonViewInformation(

    val displayContentName: String,

    @JsonSerialize(using = WindowSerializer::class)
    val windows: List<Window>,
) {
    fun toJson(): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(this)
    }
}
