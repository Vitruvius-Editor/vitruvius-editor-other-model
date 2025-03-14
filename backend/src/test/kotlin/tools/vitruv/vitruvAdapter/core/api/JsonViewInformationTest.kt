package tools.vitruv.vitruvAdapter.core.api

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.core.api.testutils.JsonNormalizer
import tools.vitruv.vitruvAdapter.core.api.testutils.TestTextDisplayContentMapper

class JsonViewInformationTest {
    @Test
    fun testTextMappingParseWindowsToJsonViewInformation() {
        val displayContentMapper = TestTextDisplayContentMapper()
        val viewInformation = JsonViewInformation(displayContentMapper)
        val window = Window("window1", "content1")
        val window2 = Window("window2", "content2")

        val expectedJson =
            """
            {
              "visualizerName": "TestVisualizer",
              "windows": [
                { "name": "window1", "content": "content1" },
                { "name": "window2", "content": "content2" }
              ]
            }
            """.trimIndent()

        val serializedJson = viewInformation.parseWindowsToJson(listOf(window, window2)) // Your JSON serialization method here
        assertEquals(JsonNormalizer.normalize(expectedJson), JsonNormalizer.normalize(serializedJson))
    }

    @Test
    fun testParseWindowsFromJson() {
        val displayContentMapper = TestTextDisplayContentMapper()
        val viewInformation = JsonViewInformation(displayContentMapper)
        val window = Window("window1", "content1")
        val window2 = Window("window2", "content2")

        val json =
            """
            {
              "visualizerName": "TestVisualizer",
              "windows": [
                { "name": "window1", "content": "content1" },
                { "name": "window2", "content": "content2" }
              ]
            }
            """.trimIndent()

        val windows = viewInformation.parseWindowsFromJson(json)
        assertEquals(2, windows.size)
        assertEquals(window, windows[0])
        assertEquals(window2, windows[1])
    }

    @Test
    fun testCollectWindowsFromJson() {
        val displayContentMapper = TestTextDisplayContentMapper()
        val viewInformation = JsonViewInformation(displayContentMapper)

        val json =
            """
            {
              "visualizerName": "TestVisualizer",
              "windows": [
                { "name": "window1", "content": "content1" },
                { "name": "window2", "content": "content2" }
              ]
            }
            """.trimIndent()

        val windows = viewInformation.collectWindowsFromJson(json)

        val expectedWindows = setOf("window1", "window2")
        assertEquals(2, windows.size)
        assertEquals(expectedWindows, windows)
    }
}
