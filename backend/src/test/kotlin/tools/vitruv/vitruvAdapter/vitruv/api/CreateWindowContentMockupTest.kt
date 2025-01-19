package tools.vitruv.vitruvAdapter.vitruv.api
import io.swagger.v3.core.util.Json
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.impl.EClassImpl
import org.eclipse.emf.ecore.impl.EFactoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.vitruv.api.testutils.JavaClassViewMapper
import tools.vitruv.vitruvAdapter.vitruv.api.testutils.JsonNormalizer

class CreateWindowContentMockupTest {

    lateinit var eObjects: List<EObject>

    @BeforeEach
    fun initEObjects() {
        val testEClass = EcoreFactory.eINSTANCE.createEClass()
        testEClass.name = "EClass"
        testEClass.eAttributes.add(EcoreFactory.eINSTANCE.createEAttribute().apply {
            name = "test"
            eType = EcoreFactory.eINSTANCE.createEDataType().apply {
                instanceClassName = "int"
            }
        })

        val testEClass2 = EcoreFactory.eINSTANCE.createEClass()
        testEClass2.name = "EClass2"
        testEClass2.eAttributes.add(EcoreFactory.eINSTANCE.createEAttribute().apply {
            name = "test2"
            eType = EcoreFactory.eINSTANCE.createEDataType().apply {
                instanceClassName = "String"
            }
        })
        eObjects = listOf(testEClass, testEClass2)

    }


    @Test
    fun `test mapping and JSON serialization`() {
        // Given: A JavaClassViewMapper
        val mapper = JavaClassViewMapper()

        // When: We map the EObjects to Windows
        val windows = mapper.mapViewToContentData(eObjects)

        // Then: We should get our Windows with the correct content
        assertEquals(2, windows.size)
        assertEquals("EClass", windows[0].name)
        assertEquals("EClass2", windows[1].name)
        assertEquals("public class EClass {\n  test: int\n}", windows[0].content)
        assertEquals("public class EClass {\n  test2: String\n}", windows[1].content)

        val jsonViewInformation = JsonViewInformation(mapper.getDisplayContent())
        val serializedJson = jsonViewInformation.toJson(windows)
        val expectedJson = """
        {
          "visualizerName": "textVisualizer",
          "windows": [
            { "name": "EClass", "content": "public class EClass {\n  test: int\n}" },
            { "name": "EClass2", "content": "public class EClass {\n  test2: String\n}" }
          ]
        }
        """.trimIndent()
        assertEquals(
            JsonNormalizer.normalize(expectedJson),
            JsonNormalizer.normalize(serializedJson)
        )

    }

    @Test
    fun `test mapContentDataToView`() {

    }
}