package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableContentSelector
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableEntry
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram
import tools.vitruv.vitruvAdapter.utils.EObjectContainer
import java.io.FileInputStream
import java.io.InputStream

class ClassDiagramContentSelectorTest {
 private val selector = ClassDiagramContentSelector()

 /**
  * @author Patrick & Amir
  */
 @Test fun testApplySelection() {
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