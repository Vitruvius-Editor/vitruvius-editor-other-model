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
        val container = EObjectContainer().getContainerWith2Packages()
        val selectedObjects = selector.applySelection(container, setOf("examplePackage", "examplePackage2"))
        val javaPackage1 = container[2]
        val javaPackage2 = container[3]
        val umlPackage1 = container[0]
        val umlPackage2 = container[1]
        val classTableWindow = PreMappedWindow<TableDTO<ClassTableEntry>>("examplePackage", mutableListOf(umlPackage1, javaPackage1))
        val classTableWindow2 = PreMappedWindow<TableDTO<ClassTableEntry>>("examplePackage2", mutableListOf(umlPackage2, javaPackage2))
        val expectedSelectedWindows = listOf(
            classTableWindow,
            classTableWindow2
        )
        assertEquals(expectedSelectedWindows, selectedObjects)
    }
}