package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.model.java.containers.JavaRoot
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO
import tools.vitruv.vitruvAdapter.utils.EObjectContainer
import java.io.FileInputStream
import java.io.InputStream
import kotlin.test.assertEquals

class ClassTableViewMapperTest {
    private lateinit var preMappedWindows: List<PreMappedWindow<TableDTO<ClassTableEntry>>>
    private lateinit var preMappedWindowsNested: List<PreMappedWindow<TableDTO<ClassTableEntry>>>
    private lateinit var eObjectPreMappedWindows: List<PreMappedWindow<TableDTO<ClassTableEntry>>>

    private lateinit var eObjects: List<EObject>
    private lateinit var eObjectsNotAPackage: List<EObject>
    private lateinit var eObjectsNestedPackage: List<EObject>

    private val mapper = ClassTableViewMapper()

    @BeforeEach
    fun initEOBjects() {
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
        val attribute = umlClass.createOwnedAttribute("myIntAttribute", null)
        attribute.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL

        val inputStream: InputStream =
            FileInputStream("src/test/kotlin/tools/vitruv/vitruvAdapter/utils/class1")
        val rootw = JaMoPPJDTSingleFileParser().parse("class1", inputStream) as CompilationUnit

        val class1 = rootw.classifiers[0] as tools.mdsd.jamopp.model.java.classifiers.Class
        val class1packageName = class1.`package`.name

        preMappedWindows =
            ClassTableContentSelector().applySelection(
                listOf(examplePackage, rootw),
                setOf(
                    "examplePackage",
                    "Class1",
                    class1packageName,
                ),
            )
        preMappedWindowsNested =
            ClassTableContentSelector().applySelection(
                listOf(examplePackageNested, rootw),
                setOf("examplePackageNested"),
            )

        eObjects = EObjectContainer().getContainer3AsRootObjects()
        eObjectPreMappedWindows = listOf(PreMappedWindow("examplePackage", eObjects as MutableList<EObject>))
        eObjectsNotAPackage = listOf(examplePackageImport, examplePackageImport, rootw)
        eObjectsNestedPackage = listOf(examplePackageNested, rootw)
    }

    @Test
    fun testMapViewToWindows() {
        val windows = mapper.mapViewToWindows(eObjects)
        val expectedWindows = setOf<String>("examplePackage")
        assertEquals(expectedWindows, windows)
    }

    @Test
    fun testWindowsNotAPackage() {
        val windows = mapper.mapViewToWindows(eObjectsNotAPackage)
        assertEquals(emptySet<String>(), windows)
    }

    @Test
    fun testWindowsNestedPackage() {
        val windows = mapper.mapViewToWindows(eObjectsNestedPackage)
        val expectedWindows = setOf<String>("examplePackageNested", "nestedPackage", "examplePackage")
        assertEquals(expectedWindows, windows)
    }

    @Test
    fun testCreateContent() {
        val contents = mapper.mapEObjectsToWindowsContent(eObjectPreMappedWindows)
        assertEquals("Class1", contents[0].content.rows[0].name)
        assertEquals(5, contents[0].content.rows[0].linesOfCode)
    }

    @Test
    fun testEditContent() {
        val contents = mapper.mapEObjectsToWindowsContent(preMappedWindows)
        val newContents = mapper.mapWindowsToEObjectsAndApplyChangesToEObjects(preMappedWindows, contents)
        println(newContents)
    }

    @Test
    fun testJroot() {
        val inputStream2: InputStream =
            FileInputStream("src/test/kotlin/tools/vitruv/vitruvAdapter/utils/class1")
        val jroot = JaMoPPJDTSingleFileParser().parse("class1", inputStream2) as JavaRoot
        val factory = UMLFactory.eINSTANCE
        val jrExp = factory.createPackage()
        jrExp.name = "jrExp"

        val preMappedJroot =
            ClassTableContentSelector().applySelection(listOf(jrExp, jroot), setOf(jroot.name, jrExp.name, "Class1"))
        val contents = mapper.mapEObjectsToWindowsContent(preMappedJroot)
        val newContents = mapper.mapWindowsToEObjectsAndApplyChangesToEObjects(preMappedJroot, contents)
        println(newContents)
    }

    @Test
    fun testGetDisplayContent() {
        assertEquals("TableVisualizer", mapper.getDisplayContent().getVisualizerName())
    }
}
