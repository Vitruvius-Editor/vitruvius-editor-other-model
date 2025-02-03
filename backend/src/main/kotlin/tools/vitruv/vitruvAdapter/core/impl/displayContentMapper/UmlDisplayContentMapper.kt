package tools.vitruv.vitruvAdapter.core.impl.displayContentMapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.VisualizerType
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram

class UmlDisplayContentMapper: DisplayContentMapper<UmlDiagram> {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    /**
     * This function is used to parse the content of a window to a string.
     * @param content the content of the window
     * @return the string representation of the content
     */
    override fun parseContent(content: UmlDiagram): String {
        return objectMapper.writeValueAsString(content)
    }

    /**
     * This function is used to parse the string representation of the content to the content itself.
     * @param content the string representation of the content
     * @return the content itself
     */
    override fun parseString(content: String): UmlDiagram {
        return objectMapper.readValue(content, UmlDiagram::class.java)
    }

    /**
     * This function is used to get the name of the visualizer that is used to display the content.
     * Note that Visualizer and ContentMapper should have a 1:1 relationship.
     * @return the name of the visualizer
     */
    override fun getVisualizerName(): String {
        return VisualizerType.UML_VISUALIZER.visualizerName
    }
}