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
        val jsonViewInformation = JsonViewInformation("displayContentName", listOf(window))
        assertEquals("{\"displayContentName\":\"$name\",\"windows\":[{\"windowName\":\"windowName\",\"content\":\"$content\"}]}", jsonViewInformation.toJson())
    }

}