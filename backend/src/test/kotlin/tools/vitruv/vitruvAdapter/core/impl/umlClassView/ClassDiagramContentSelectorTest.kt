package tools.vitruv.vitruvAdapter.core.impl.umlClassView

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
 @Test
 fun testApplySelection() {
  val container = EObjectContainer.getContainerWith2Packages()
  val selectedObjects = selector.applySelection(container, setOf("examplePackage", "examplePackage2"))
  val umlPackage1 = container[2]
  val umlPackage2 = container[3]
  val classTableWindow = PreMappedWindow<UmlDiagram>("examplePackage", mutableListOf(umlPackage1))
    val classTableWindow2 = PreMappedWindow<UmlDiagram>("examplePackage2", mutableListOf(umlPackage2))
  val expectedSelectedWindows = listOf(
   classTableWindow,
    classTableWindow2
  )
  assertEquals(expectedSelectedWindows, selectedObjects)
 }
}