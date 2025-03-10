package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.uml.*
import tools.vitruv.vitruvAdapter.utils.EObjectContainer
import tools.vitruv.vitruvAdapter.utils.EResourceMock
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

        eObjects = EObjectContainer().getContainer3AsRootObjects()
        eObjectsNotAPackage = listOf(examplePackageImport, examplePackageImport, rootw)
        eObjectsNestedPackage = listOf(examplePackageNested, rootw)
        eObjectsClassExtends = EObjectContainer().getContainerWithClassExtends()

    }

    @Test
    fun testMapViewToWindows() {
        val windows = mapper.mapViewToWindows(eObjects)
        val expectedWindows = setOf("examplePackage")
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
        val container = EObjectContainer().getUmlContainerWithInterfaceRealization()
        val containerPackage = container[0] as Package
        val useLessClass = UMLFactory.eINSTANCE.createClass()
        useLessClass.name = "UselessClass"
        val mutableList = container.toMutableList()
        mutableList.add(useLessClass)

        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", mutableList)

        println(mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow)))

        val classUUID = EResourceMock.getFakeUUID(getPackageAbleElement("Class1", containerPackage))
        val interfaceUUID = EResourceMock.getFakeUUID(getPackageAbleElement("Interface1", containerPackage))
        val method = ((containerPackage).ownedElements[0] as Class).ownedOperations[0]
        val methodUUID = EResourceMock.getFakeUUID(method)


        val umlMethod =
            UmlMethod(methodUUID, UmlVisibility.PUBLIC, "myIntMethod", listOf(), UmlType("PrimitiveType", "int"))
        val umlConnection = UmlConnection(
            classUUID + interfaceUUID,
            classUUID,
            interfaceUUID,
            UmlConnectionType.IMPLEMENTS,
            "",
            "",
            ""
        )

        val nodes = listOf(
            UmlNode(
                EResourceMock.getFakeUUID(getPackageAbleElement("Class1", container[0] as Package)),
                "Class2",
                "<<class>>",
                listOf(
                    UmlAttribute(getFakeUUID(getAttribute("myIntAttribute", getClass("Class1", containerPackage))), UmlVisibility.PUBLIC, "myIntAttribute2", UmlType("PrimitiveType", "int"))
                ),
                listOf(umlMethod),
                listOf()
            ),
            UmlNode("Class3", "Class3", "<<class>>", listOf(), listOf(umlMethod), listOf()),

            UmlNode(
                EResourceMock.getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                "Interface2",
                "<<interface>>",
                listOf(),
                listOf(),
                listOf()
            ),
            UmlNode("Interface", "Interface1", "<<interface>>", listOf(), listOf(), listOf()),
        )

        val connections = listOf(
            UmlConnection("Class%Interface", "Class", "Interface", UmlConnectionType.IMPLEMENTS, "", "", ""),
            umlConnection
        )

        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.mapWindowsToEObjectsAndApplyChangesToEObjects(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram))
        )
        println(mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow)))
    }

    /**
     * Test if the deletion of a class, an attribute, an operation and an interface operation works.
     */
    @Test
    fun testDeleteObjects() {
        val container = EObjectContainer().getUmlContainerWithInterfaceRealization()
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes = listOf(
            UmlNode(
                getFakeUUID(getPackageAbleElement("Class1", containerPackage)),
                "Class1",
                "<<class>>",
                listOf(
                ),
                listOf(),
                listOf()
            ),
            UmlNode(
                getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                "Interface1",
                "<<interface>>",
                listOf(),
                listOf(),
                listOf()
            )
        )

        val connections = listOf<UmlConnection>()
        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.mapWindowsToEObjectsAndApplyChangesToEObjects(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram))
        )
        println(mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow)))



    }

    /**
     * Test if the deletion of a parameter in methods works.
     */
    @Test
    fun testDeleteMoreObjects() {
        val eObjectContainer = EObjectContainer()
        val container = eObjectContainer.getUmlContainerWith(listOf(eObjectContainer.getUmlClassWithMethod()))
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes = listOf(
            UmlNode(
                getFakeUUID(getPackageAbleElement("Class2", containerPackage)),
                "Class2",
                "<<class>>",
                listOf(
                ),
                listOf(
                    UmlMethod(getFakeUUID(getOperation("myMethod", getClass("Class2", containerPackage))),
                        UmlVisibility.PUBLIC,
                        "myMethod",
                        listOf(), UmlType("PrimitiveType", "int"))

                ),
                listOf()
            )
        )

        val connections = listOf<UmlConnection>()
        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.mapWindowsToEObjectsAndApplyChangesToEObjects(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram))
        )
        println(mapper.mapEObjectsToWindowsContent(listOf(preMappedWindow)))


    }

    private fun getFakeUUID(eObject: EObject): String {
        return EResourceMock.getFakeUUID(eObject)
    }

    private fun getPackageAbleElement(packageAbleElementName: String, umlPackage: Package): PackageableElement {
        return umlPackage.packagedElements.find { (it is Class || it is Interface) && it.name == packageAbleElementName }!!
    }

    private fun getClass(className: String, umlPackage: Package): Class{
        return umlPackage.packagedElements.find { it is Class && it.name == className } as Class
    }

    private fun getInterface(interfaceName: String, umlPackage: Package): Interface{
        return umlPackage.packagedElements.find { it is Interface && it.name == interfaceName } as Interface
    }

    private fun getAttribute(propertyName: String, umlClass: Class): Property{
        return umlClass.members.find { it is Property && it.name == propertyName } as Property
    }

    private fun getOperation(operationName: String, umlClass: Class): Operation{
        return umlClass.members.find { it is Operation && it.name == operationName } as Operation
    }

    private fun getOperation(operationName: String, umlInterface: Interface): Operation {
        return umlInterface.ownedOperations.find { it.name == operationName } as Operation
    }

    private fun getParameter(parameterName: String, umlOperation: Operation): Parameter{
        return umlOperation.ownedParameters.find { it.name == parameterName } as Parameter
    }


}
