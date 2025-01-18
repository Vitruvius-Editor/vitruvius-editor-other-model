package tools.vitruv.vitruvAdapter.vitruv.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

/**
 * This class is responsible for serializing the visualizer information and the list of windows to a json string.
 * @author uhsab
 */
class JsonViewInformation(
    private val visualizerName: String,
) {

    /**
     * Serializes the visualizer information and the list of windows to a json string.
     * @param windows The list of windows.
     * @param displayContentMapper The DisplayContent instance used for parsing window content.
     * @return The generated json string.
     */
    fun <E> toJson(windows: List<Window<E>>, displayContentMapper: DisplayContentMapper<E>): String {
        // Map each Window to a SerializableWindow by parsing its content.
        val serializableWindows = windows.map { window ->
            SerializableWindow(
                name = window.name,
                content = displayContentMapper.parseContent(window.content)
            )
        }

        // Create a data structure to hold the full JSON content.
        val output = mapOf(
            "visualizerName" to visualizerName,
            "windows" to serializableWindows
        )

        val objectMapper = ObjectMapper().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
        return objectMapper.writeValueAsString(output)
    }

}
