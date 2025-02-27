package tools.vitruv.vitruvAdapter.core.impl.classTableView

import jakarta.persistence.Table
import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO
import java.io.FileInputStream
import java.io.InputStream

class ClassTableViewMapperTest {

    lateinit var preMappedWindows: List<PreMappedWindow<TableDTO<ClassTableEntry>>>
    lateinit var eObjects: List<EObject>
    val mapper = ClassTableViewMapper()

    @BeforeEach
    fun initEOBjects() {

        val factory = UMLFactory.eINSTANCE
        val examplePackage = factory.createPackage()
        examplePackage.name = "examplePackage"

        val umlClass = examplePackage.createOwnedClass("Class1", false)
        val class2 = examplePackage.createOwnedClass("Class2", true)


        val attribute = umlClass.createOwnedAttribute("myIntAttribute", null)


        attribute.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL

        val inputStream: InputStream =
            FileInputStream("src/test/kotlin/tools/vitruv/vitruvAdapter/core/impl/classTableView/class1")
        val rootw = JaMoPPJDTSingleFileParser().parse("class1", inputStream) as CompilationUnit
        val class1 = rootw.classifiers[0] as tools.mdsd.jamopp.model.java.classifiers.Class
        val packageName = class1.`package`.name








        preMappedWindows =
            ClassTableContentSelector().applySelection(listOf(examplePackage, rootw), setOf("examplePackage"))
        eObjects = listOf(examplePackage, rootw)

    }

    @Test
    fun testWindows() {
        val windows = mapper.mapViewToWindows(eObjects)
        windows.forEach(::println)
    }

    @Test
    fun testCreateContent() {
        val contents = mapper.mapEObjectsToWindowsContent(preMappedWindows)
        for (content in contents) {
            println(content.content.toString())
        }
        print(eObjects)
    }

    @Test
    fun testEditContent() {
        val contents = mapper.mapEObjectsToWindowsContent(preMappedWindows)
        val newContents = mapper.mapWindowsToEObjectsAndApplyChangesToEObjects(preMappedWindows, contents)
        println(newContents)
    }


    @Test
    fun testGetDisplayContent() {
        println(mapper.getDisplayContent())
    }


}