package tools.vitruv.vitruvAdapter.vitruv.api
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.vitruv.api.testutils.JavaClassViewMapper
import tools.vitruv.vitruvAdapter.vitruv.api.testutils.MockJavaClass

class JavaClassViewMapperTest {

    @Test
    fun `test mapping and JSON serialization`() {
        // Given: A list of MockJavaClass objects
        val rootObjects = listOf(
            MockJavaClass("MyClass1"),
            MockJavaClass("MyClass2")
        )

        // And: A JavaClassViewMapper
        val mapper = JavaClassViewMapper()

        // When: We map them to content data (Windows)
        val windows = mapper.mapViewToContentData(rootObjects)
        assertEquals(2, windows.size)
        assertEquals("MyClass1", windows[0].name)
        assertTrue(windows[0].content.contains("public class MyClass1"))
        assertEquals("MyClass2", windows[1].name)

        // And: Convert the windows to JSON
        val viewInformation = JsonViewInformation(mapper.getDisplayContent())
        val jsonOutput = viewInformation.toJson(windows)

        // Then: JSON should contain our class names
        println(jsonOutput)
        assertTrue(jsonOutput.contains("MyClass1"), "JSON should include 'MyClass1'")
        assertTrue(jsonOutput.contains("MyClass2"), "JSON should include 'MyClass2'")
        assertTrue(jsonOutput.contains("TestVisualizer"), "JSON should include our visualizer name")

        // You can also do more complex JSON structure checks, e.g., by parsing the JSON back into a data object.
    }

    @Test
    fun `test mapContentDataToView`() {
        // Given: Some Windows with string content
        val windows = listOf(
            Window(name = "ClassA", content = "public class ClassA {}"),
            Window(name = "ClassB", content = "public class ClassB {}")
        )

        val mapper = JavaClassViewMapper()

        // When: We map them back to EObjects
        val eObjects = mapper.mapContentDataToView(windows)

        // Then: We should get our MockJavaClass objects
        assertEquals(2, eObjects.size)
        assertEquals("ClassA", (eObjects[0] as MockJavaClass).className)
        assertEquals("ClassB", (eObjects[1] as MockJavaClass).className)
    }
}