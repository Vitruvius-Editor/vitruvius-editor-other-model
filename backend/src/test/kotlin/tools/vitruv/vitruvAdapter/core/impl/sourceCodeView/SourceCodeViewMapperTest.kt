package tools.vitruv.vitruvAdapter.core.impl.sourceCodeView

import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.formatter.CodeFormatter
import org.eclipse.jface.text.Document
import org.eclipse.text.edits.TextEdit
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.Package
import tools.vitruv.vitruvAdapter.utils.EObjectContainer


class SourceCodeViewMapperTest {

    private val mapper = SourceCodeViewMapper()

    @BeforeEach
    fun initObjects() {


    }

    @Test
    fun testMapToWindows() {
        val windows = mapper.mapViewToWindows(EObjectContainer.getContainer1AsRootObjects())
        for (container1AsRootObject in EObjectContainer.getContainer1AsRootObjects()) {
            if (container1AsRootObject is Package) {
                println(container1AsRootObject.classifiers)
            }
        }
        println(windows)
        val windows2 = mapper.mapViewToWindows(EObjectContainer.getContainer2())
        println(windows2)
        val windows3 = mapper.mapViewToWindows(EObjectContainer.getContainer3())
        println(windows3)
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