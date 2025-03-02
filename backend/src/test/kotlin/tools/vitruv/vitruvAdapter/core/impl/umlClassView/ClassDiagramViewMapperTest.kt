package tools.vitruv.vitruvAdapter.core.impl.umlClassView
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram
import tools.vitruv.vitruvAdapter.utils.EObjectContainer

class ClassDiagramViewMapperTest {

 private val mapper = ClassDiagramViewMapper()

 /**
  * @author Patrick & Amir
  */
 @Test
 fun testMapToWindows() {
  val windows = mapper.mapViewToWindows(EObjectContainer.getContainer3AsRootObjects())
  assertEquals(setOf<String>("examplePackage"), windows)
 }

 /**
  * @author Patrick
  */
 @Test
 fun testMapEObjectsToWindowsContent() {
    val preMappedWindow1 = PreMappedWindow<UmlDiagram>("examplePackage", EObjectContainer.getContainer3AsRootObjects().toMutableList())
    val window1 = mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow1))
    print(window1)
 }

 /**
  * @author Nico,Amir
  */
 @Test
 fun testEditWindowContent() {

 }


}