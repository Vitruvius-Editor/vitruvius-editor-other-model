package tools.vitruv.vitruvAdapter.vitruv.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.module.SimpleModule

class JsonViewInformation(

    val displayContentName: String,

    val windows: List<Window>,
) {
    fun toJson(): String {
        val module = SimpleModule()
        module.addSerializer(Window::class.java, WindowSerializer())
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(module)
        return objectMapper.writeValueAsString(this)
    }
}
