package tools.vitruv.vitruvAdapter.vitruv.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import tools.vitruv.vitruvAdapter.vitruv.api.testutils.TestTextDisplayContentMapper

class JsonViewInformationTest {

    
    @org.junit.jupiter.api.Test
    fun testTextMappingToJsonViewInformation() {
        val displayContentMapper = TestTextDisplayContentMapper()
        val viewInformation = JsonViewInformation(displayContentMapper)
        val window = Window<String>("window1", "content1")
        val window2 = Window<String>("window2", "content2")


        val expectedJson = """
        {
          "visualizerName": "textVisualizer",
          "windows": [
            { "name": "window1", "content": "content1" },
            { "name": "window2", "content": "content2" }
          ]
        }
        """.trimIndent()

        val serializedJson = viewInformation.toJson(listOf(window,window2))/* Your JSON serialization method here */
        val objectMapper = ObjectMapper()
        // Normalize both JSON strings to remove formatting differences
        val normalizedExpectedJson = objectMapper.writeValueAsString(
            objectMapper.readTree(expectedJson)
        )
        val normalizedActualJson = objectMapper.writeValueAsString(
            objectMapper.readTree(serializedJson)
        )

        // Compare the normalized strings
        assertEquals(normalizedExpectedJson, normalizedActualJson)
    }



}


