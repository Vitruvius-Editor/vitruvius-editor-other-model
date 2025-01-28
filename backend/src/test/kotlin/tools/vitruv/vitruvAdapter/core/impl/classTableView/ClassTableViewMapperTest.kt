package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClassTableViewMapperTest {

 lateinit var eobjects : List<EObject>
 @BeforeEach
 fun initEOBjects() {

    val factory = UMLFactory.eINSTANCE
    val examplePackage = factory.createPackage()
    examplePackage.name = "examplePackage"

    val umlClass = examplePackage.createOwnedClass("ExampleModel", false)
     val class2 = examplePackage.createOwnedClass("ExampleModel2", false)

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
         println(content.content)
     }
  }
}