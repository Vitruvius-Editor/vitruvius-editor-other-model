package tools.vitruv.vitruvAdapter.vitruv.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

/**
 * This class is responsible for serializing the visualizer information and the list of windows to a json string.
 * @author uhsab
 */
class JsonViewInformation<E>(
    private val displayContentMapper: DisplayContentMapper<E>,
) {

    /**
     * Serializes the visualizer information and the list of windows to a json string.
     * @param windows The list of windows.
     * @param displayContentMapper The DisplayContent instance used for parsing window content.
     * @return The generated json string.
     */
    fun toJson(windows: List<Window<E>>, ): String {
        // Map each Window to a SerializableWindow by parsing its content.
        val serializableWindows = windows.map { window ->
            SerializableWindow(
                name = window.name,
                content = displayContentMapper.parseContent(window.content)
            )
        }

        // Create a data structure to hold the full JSON content.
        val output = mapOf(
            "visualizerName" to displayContentMapper.getVisualizerName(),
            "windows" to serializableWindows
        )

        val objectMapper = ObjectMapper().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
        return objectMapper.writeValueAsString(output)
    }

    fun fromJson(json: String): List<Window<E>> {
        val objectMapper = ObjectMapper()
        val jsonNode = objectMapper.readTree(json)
        val visualizerName = jsonNode.get("visualizerName").asText()
        val windows = jsonNode.get("windows").map { windowNode ->
            val name = windowNode.get("name").asText()
            val content = displayContentMapper.parseString(windowNode.get("content").asText())
            Window(name, content)
        }
        return windows
    }

}
