package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram
import tools.vitruv.vitruvAdapter.utils.EObjectContainer

class ClassDiagramContentSelectorTest {
    private val selector = ClassDiagramContentSelector()

    /**
     * @author Patrick & Amir
     */
    @Test fun testApplySelection() {
        val container = EObjectContainer().getContainerWith2Packages()
        val umlPackage1 = container[2] as Package
        val umlPackage2 = container[3] as Package
        val umlPackage3 = UMLFactory.eINSTANCE.createPackage()
        umlPackage3.name = "examplePackage3"
        umlPackage1.nestedPackages.add(umlPackage3)
        val umlPackage4 = UMLFactory.eINSTANCE.createPackage()
        umlPackage4.name = "examplePackage4"
        umlPackage2.nestedPackages.add(umlPackage4)
        val selectedObjects = selector.applySelection(container, setOf("examplePackage", "examplePackage3"))
        val classTableWindow = PreMappedWindow<UmlDiagram>("examplePackage", mutableListOf(umlPackage1))
        val classTableWindow3 = PreMappedWindow<UmlDiagram>("examplePackage3", mutableListOf(umlPackage3))
        val expectedSelectedWindows =
            listOf(
                classTableWindow,
                classTableWindow3,
            )
        assertEquals(expectedSelectedWindows, selectedObjects)
    }
}
