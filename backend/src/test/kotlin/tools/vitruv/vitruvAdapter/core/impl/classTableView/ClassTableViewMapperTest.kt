package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO

class ClassTableViewMapperTest {

 lateinit var eobjects : List<EObject>
 @BeforeEach
 fun initEOBjects() {

    val factory = UMLFactory.eINSTANCE
    val examplePackage = factory.createPackage()
    examplePackage.name = "examplePackage"

    val umlClass = examplePackage.createOwnedClass("class1", false)
     val class2 = examplePackage.createOwnedClass("class2", true)

    val attribute = umlClass.createOwnedAttribute("myIntAttribute", null)
    attribute.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL

     eobjects = listOf(examplePackage)
 }

 @Test
 fun testCreateContent() {
  val mapper = ClassTableViewMapper()
  val windows = mapper.mapViewToWindows(eobjects)
  windows.forEach(::println)

  val contents = mapper.mapEObjectsToWindowsContent(eobjects)
     for (content in contents) {
         println(content.content.toString())
     }


     val newClass1DTO = ClassTableEntry("", "Class1", "private", false, false, "", listOf(), 1, 0, 0)
     val newClass2DTO = ClassTableEntry("", "Class2", "public", true, false, "", listOf(), 0, 0, 0)
     val newWindow = Window("examplePackage", TableDTO.buildTableDTO(listOf(newClass1DTO, newClass2DTO), ClassTableEntry::class))
     mapper.mapWindowsToEObjectsAndApplyChangesToEObjects(eobjects, listOf(newWindow))

     print(eobjects)

  }


}