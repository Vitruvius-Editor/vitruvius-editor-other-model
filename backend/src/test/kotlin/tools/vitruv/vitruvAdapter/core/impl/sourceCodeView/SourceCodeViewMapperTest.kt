package tools.vitruv.vitruvAdapter.core.impl.sourceCodeView

import org.eclipse.emf.ecore.EObject
import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.formatter.CodeFormatter
import org.eclipse.jface.text.Document
import org.eclipse.text.edits.TextEdit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import java.io.FileInputStream


class SourceCodeViewMapperTest {
    private lateinit var eObjects: List<EObject>

    val mapper = SourceCodeViewMapper()

    @BeforeEach
    fun initObjects() {
        try {
            val inputStream =
                FileInputStream("src/test/kotlin/tools/vitruv/vitruvAdapter/core/impl/sourceCodeView/TestClass.txt")
            val compilationUnit = JaMoPPJDTSingleFileParser().parse("TestClass.txt", inputStream) as CompilationUnit
            eObjects = listOf(compilationUnit)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Test
    fun testMapEObjectsToWindowsContent() {
        val sourceCodeSelector = SourceCodeContentSelector()
        val views = mapper.mapViewToWindows(eObjects)
        assertEquals(2, views.size)
        val selectedEObjects = sourceCodeSelector.applySelection(eObjects, views)
        val selectedContent = mapper.mapEObjectsToWindowsContent(selectedEObjects)
        val firstClassContent = selectedContent[0].content
        val expectedFirstClassContent = "public class TestClass {" +
                "\n" +
                "    int a = 0;\n" +
                "\n" +
                "    int b = 1;\n" +
                "\n" +
                "    int c = 2 +54;\n" +
                "\n" +
                "    public void testMethod() {\n" +
                "        System.out.println(\"Hello World!\");\n" +
                "    }\n" +
                "\n" +
                "    public void testMethod2() {\n" +
                "        System.out.println(\"Hello World!\");\n" +
                "    }\n" +
                "\n" +
                "    public void testMethod3() {\n" +
                "        System.out.println(\"Hello World!\");\n" +
                "    }\n" +
                "\n" +
                "}\n" +
                "\n"

        assertEquals(formatJavaCode(expectedFirstClassContent), firstClassContent)
    }

    fun formatJavaCode(code: String): String? {
        // Create a CodeFormatter with default options (you can also supply a map for custom settings)
        val formatter: CodeFormatter = ToolFactory.createCodeFormatter(null)
        // Format the code as a compilation unit (use CodeFormatter.K_COMPILATION_UNIT)
        val textEdit: TextEdit? = formatter.format(
            CodeFormatter.K_COMPILATION_UNIT,
            code,
            0,
            code.length,
            0,
            null
        )

        return (if (textEdit != null) {
            val document = Document(code)
            textEdit.apply(document)
            document.get()
        } else {
            null // formatting failed
        }).toString()
    }

    @Test
    fun testMapWindowContentToEObjects() {
        //TODO: Implement test
    }

    @Test
    fun tesMapViewToWindows() {
        val windows = mapper.mapViewToWindows(this.eObjects)
        val expected = mutableSetOf<String>()
        expected.add("TestClass")
        expected.add("TestClass2")
        assertEquals(expected, windows)

    }

}