package tools.vitruv.vitruvAdapter.core.impl.sourceCodeView

import org.eclipse.emf.ecore.EObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import java.io.FileInputStream


class SourceCodeContentSelectorTest {
    private lateinit var eObjects: MutableList<EObject>
    private lateinit var expectedEObjects : MutableList<EObject>

    @BeforeEach
    fun initObjects() {
        try {
            val inputStream =
                FileInputStream("src/test/kotlin/tools/vitruv/vitruvAdapter/core/impl/sourceCodeView/TestClass.txt")
            val compilationUnit = JaMoPPJDTSingleFileParser().parse("TestClass.txt", inputStream) as CompilationUnit
            eObjects = mutableListOf(compilationUnit)
            expectedEObjects = mutableListOf((compilationUnit as CompilationUnit).classifiers.first())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Test
    fun testApplySelection() {
        val sourceCodeSelector = SourceCodeContentSelector()
        val selectedObjects = sourceCodeSelector.applySelection(eObjects, setOf("TestClass"))

        val expectedObjects = listOf(PreMappedWindow<String>("TestClass", expectedEObjects))
        assertEquals(expectedObjects, selectedObjects)
    }

}