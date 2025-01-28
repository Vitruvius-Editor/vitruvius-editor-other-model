package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ClassTableViewMapperTest {

 lateinit var eobjects : List<EObject>
 @BeforeEach
 fun initEOBjects() {
    val supPackage = UMLFactory.eINSTANCE.createPackage()
    supPackage.setName("supPackage")
    val package1 = UMLFactory.eINSTANCE.createPackage()
    package1.setName("package1")
    val package2 = UMLFactory.eINSTANCE.createPackage()
    package2.setName("package2")
    supPackage.packagedElements.addAll(listOf(package1, package2))




    val class1 = package1.createOwnedClass("class1", true)
    val class2 = package1.createOwnedClass("class2", false)
    val class3 = package2.createOwnedClass("class3", true)
    val class4 = package2.createOwnedClass("class4", false)

    class1.superClasses.add(class2)
    class2.superClasses.add(class3)

    val attribute1 = class1.createOwnedAttribute("attribute1", null)
    val attribute2 = class2.createOwnedAttribute("attribute2", null)


    eobjects = listOf(package1, package2)
 }

 @Test
 fun testCreateContent() {
  val mapper = ClassTableViewMapper()
  val windows = mapper.mapViewToWindows(eobjects)
  windows.forEach(::println)

  val contents = mapper.mapEObjectsToWindowsContent(eobjects)
    contents.forEach(::println)
  }
}