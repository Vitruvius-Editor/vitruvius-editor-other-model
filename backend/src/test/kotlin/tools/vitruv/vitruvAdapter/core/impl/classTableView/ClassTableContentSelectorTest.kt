package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.junit.jupiter.api.Assertions.assertEquals
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO
import tools.vitruv.vitruvAdapter.utils.EObjectContainer

class ClassTableContentSelectorTest {
    private val selector = ClassTableContentSelector()

    /**
     * @author Nico & Amir
     */
    @org.junit.jupiter.api.Test
    fun testApplySelection() {
        val javaPackage = EObjectContainer.getContainer1AsRootObjects()
        val selectedObjects = selector.applySelection(javaPackage, setOf("examplePackage"))
        val classTableWindow = PreMappedWindow<TableDTO<ClassTableEntry>>("examplePackage", javaPackage as MutableList<EObject>)
        val expectedSelectedWindows = listOf(
            classTableWindow
        )
        assertEquals(expectedSelectedWindows, selectedObjects)
    }
}