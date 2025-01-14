package tools.vitruv.vitruvAdapter.vitruv.api

import org.junit.jupiter.api.Assertions.*
class JsonViewInformationTest {
    @org.junit.jupiter.api.Test
    fun testToJson() {
        val name = "Class Diagram 1"
        val content = "I like Pineapples"
        val window = object : Window {
            override fun getWindowName(): String {
                return name
            }

            override fun getContent(): String {
                return content
            }
        }
        val jsonViewInformation = JsonViewInformation("test", listOf(window))
        val expected = "{\"displayContentName\":\"test\",\"windows\":[{\"windowName\":\"$name\",\"content\":\"$content\"}]}"
        assertEquals(expected, jsonViewInformation.toJson())
    }

}