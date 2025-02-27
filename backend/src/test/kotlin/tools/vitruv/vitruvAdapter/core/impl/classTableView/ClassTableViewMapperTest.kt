package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO
import tools.vitruv.vitruvAdapter.utils.EObjectContainer
import kotlin.test.assertEquals

class ClassTableViewMapperTest {
  private val mapper = ClassTableViewMapper()

  /**
   * @author Nico
   */
  @Test
  fun testMapToWindows() {
    val windows = mapper.mapViewToWindows(EObjectContainer.getContainer3AsRootObjects())
    assertEquals(setOf<String>("examplePackage"), windows)
  }

  /**
   * @author Nico
   */
  @Test
  fun testMapEObjectsToWindowsContent() {
    val preMappedWindow1 = PreMappedWindow<TableDTO<ClassTableEntry>>("examplePackage",
        EObjectContainer.getContainer3AsRootObjects() as MutableList<EObject>
    )
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