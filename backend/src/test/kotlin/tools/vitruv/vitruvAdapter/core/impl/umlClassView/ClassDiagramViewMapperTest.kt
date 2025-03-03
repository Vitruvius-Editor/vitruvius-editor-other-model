package tools.vitruv.vitruvAdapter.core.impl.umlClassView
import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram
import tools.vitruv.vitruvAdapter.utils.EObjectContainer
import java.io.FileInputStream
import java.io.InputStream

class ClassDiagramViewMapperTest {

 private val mapper = ClassDiagramViewMapper()

 private lateinit var eObjects: List<EObject>
 private lateinit var eObjectsNotAPackage: List<EObject>
 private lateinit var eObjectsNestedPackage: List<EObject>
 private lateinit var eObjectsClassExtends: List<EObject>

 @BeforeEach
 fun initEObjects() {

  val factory = UMLFactory.eINSTANCE
  val examplePackage = factory.createPackage()
  examplePackage.name = "examplePackage"

  val examplePackageImport = factory.createPackageImport()
  examplePackageImport.importedPackage = examplePackage

  val examplePackageNested = factory.createPackage()
  examplePackageNested.name = "examplePackageNested"
  examplePackageNested.packagedElements.add(examplePackage)
  examplePackageNested.createNestedPackage("nestedPackage")


  val umlClass = examplePackage.createOwnedClass("Class1", false)
  val umlClass2 = examplePackage.createOwnedClass("Class2", false)


  umlClass.superClasses.add(umlClass2)
  val umlInterface = examplePackage.createOwnedInterface("Interface1")

  umlClass.implementedInterfaces.add(umlInterface)


  val attribute = umlClass.createOwnedAttribute("myIntAttribute", null)
  attribute.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL

  val inputStream: InputStream =
   FileInputStream("src/test/kotlin/tools/vitruv/vitruvAdapter/utils/class1")
  val rootw = JaMoPPJDTSingleFileParser().parse("class1", inputStream) as CompilationUnit

  val class1 = rootw.classifiers[0] as tools.mdsd.jamopp.model.java.classifiers.Class
  val class1packageName = class1.`package`.name

  eObjects = EObjectContainer.getContainer3AsRootObjects()
  eObjectsNotAPackage = listOf(examplePackageImport, examplePackageImport, rootw)
  eObjectsNestedPackage = listOf(examplePackageNested, rootw)
  eObjectsClassExtends = EObjectContainer.getContainerWithClassExtends()

 }

 @Test
 fun testMapViewToWindows() {
  val windows = mapper.mapViewToWindows(eObjects)
  val expectedWindows = setOf<String>("examplePackage")
  kotlin.test.assertEquals(expectedWindows, windows)
 }

 @Test
 fun testWindowsNotAPackage() {
  val windows = mapper.mapViewToWindows(eObjectsNotAPackage)
  kotlin.test.assertEquals(emptySet<String>(), windows)
 }

 @Test
 fun testWindowsNestedPackage() {
  val windows = mapper.mapViewToWindows(eObjectsNestedPackage)
  val expectedWindows = setOf<String>("examplePackageNested", "nestedPackage", "examplePackage")
  kotlin.test.assertEquals(expectedWindows, windows)
 }

 /**
  * @author Patrick
  */
 @Test
 fun testMapEObjectsToWindowsContent() {
    val preMappedWindow1 = PreMappedWindow<UmlDiagram>("examplePackage", eObjectsClassExtends.toMutableList())
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