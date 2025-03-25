package tools.vitruv.vitruvAdapter.core.impl.sourceCodeView

import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.formatter.CodeFormatter
import org.eclipse.jface.text.Document
import org.eclipse.text.edits.TextEdit
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.utils.EObjectContainer
import kotlin.test.assertEquals

class SourceCodeViewMapperTest {
    private val mapper = SourceCodeViewMapper()

    @Test
    fun testMapToWindows() {
        val windows = mapper.collectWindowsFromView(EObjectContainer().getContainer1AsRootObjects())
        assertEquals(setOf<String>("Class1"), windows)
    }

    @Test
    fun testMapEObjectsToWindows() {
        val preMappedWindow1 = PreMappedWindow<String>("Class1", EObjectContainer().getContainerWithSimpleClass().toMutableList())
        val windowSourceCode =
            "public class Class1 {\r\n" +
                "\tboolean myBooleanAttribute = true;\r\n" +
                "\r\n" +
                "}\r\n"
        val expectedWindow = Window("Class1", formatJavaCode(windowSourceCode))

        val window1 = mapper.mapEObjectsToWindows(listOf(preMappedWindow1))
        assertEquals(listOf(expectedWindow), window1)
    }

    @Test
    fun testEditWindowContent() {
        val eObjectContainer = EObjectContainer()
        val javaPackage = eObjectContainer.getContainer2AsRootObjects()[0] as CompilationUnit
        val class2 = javaPackage.classifiers[1]
        val preMappedWindow2 = PreMappedWindow<String>("Class2", mutableListOf(class2))
        val newSourceCode =
            "public class Class2 {\n" +
                "\tint myIntAttribute2;\n" +
                "\n" +
                "\tint myIntAttribute3;\n" +
                "\n" +
                "\tpublic int myMethod(int myParameter) {\n" +
                "\t\tint x = 2;\n" +
                "\t\treturn 5;\n" +
                "\t}\n" +
                "\n" +
                "}\n"
        val window3 = Window("Class2", formatJavaCode(newSourceCode))
        mapper.applyWindowChangesToView(listOf(preMappedWindow2), listOf(window3))
        assertEquals(window3, mapper.mapEObjectsToWindows(listOf(preMappedWindow2))[0])
    }

    fun formatJavaCode(code: String): String {
        // Create a CodeFormatter with default options (you can also supply a map for custom settings)
        val formatter: CodeFormatter = ToolFactory.createCodeFormatter(null)
        // Format the code as a compilation unit (use CodeFormatter.K_COMPILATION_UNIT)
        val textEdit: TextEdit? =
            formatter.format(
                CodeFormatter.K_COMPILATION_UNIT,
                code,
                0,
                code.length,
                0,
                null,
            )

        return (
            if (textEdit != null) {
                val document = Document(code)
                textEdit.apply(document)
                document.get()
            } else {
                code // formatting failed
            }
        ).toString()
    }
}
