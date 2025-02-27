package tools.vitruv.vitruvAdapter.core.impl.sourceCodeView

import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.formatter.CodeFormatter
import org.eclipse.jface.text.Document
import org.eclipse.text.edits.TextEdit
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.model.java.containers.Package
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.utils.EObjectContainer


class SourceCodeViewMapperTest {

    private val mapper = SourceCodeViewMapper()

    @Test
    fun testMapToWindows() {
        val windows = mapper.mapViewToWindows(EObjectContainer.getContainer1AsRootObjects())
        println(windows)
        val windows2 = mapper.mapViewToWindows(EObjectContainer.getContainer2AsRootObjects())
        println(windows2)
        val windows3 = mapper.mapViewToWindows(EObjectContainer.getContainer3AsRootObjects())
        println(windows3)
    }

    @Test
    fun testMapEObjectsToWindowsContent() {
        val preMappedWindow1 = PreMappedWindow<String>("Class1", EObjectContainer.getContainer1().toMutableList())
        val preMappedWindow2 = PreMappedWindow<String>("Class2", EObjectContainer.getContainer2().toMutableList())
        val preMappedWindow3 = PreMappedWindow<String>("Class3", EObjectContainer.getContainer3().toMutableList())

        val window1 = mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow1))
        println(window1)

        val window2 = mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow2))
        println(window2)

        val window3 = mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow3))
        println(window3)
    }

    @Test
    fun testEditWindowContent() {
        val preMappedWindow3 = PreMappedWindow<String>("Class2", EObjectContainer.getContainer3().toMutableList())
        val window3 = Window("Class2", "public class Class2 {\n" +
                "\tint myIntAttribute2;\n" +
                "\n" +
                "\tint myIntAttribute3;\n" +
                "\n" +
                "\tpublic int myMethod(int myParameter) {\n" +
                "\t\tint x = 2;\n" +
                "\t\treturn 5;\n" +
                "\t}\n" +
                "\n" +
                "}")
        mapper.mapWindowsToEObjectsAndApplyChangesToEObjects(listOf(preMappedWindow3), listOf(window3))
        println(mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow3)))


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


}