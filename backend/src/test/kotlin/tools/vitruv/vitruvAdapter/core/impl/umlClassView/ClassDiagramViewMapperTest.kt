package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.VisualizerType
import tools.vitruv.vitruvAdapter.core.impl.uml.*
import tools.vitruv.vitruvAdapter.utils.EObjectContainer
import tools.vitruv.vitruvAdapter.utils.EResourceMock
import java.io.FileInputStream
import java.io.InputStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    fun testCollectWindowsFromView() {
        val windows = mapper.collectWindowsFromView(eObjects)
        val expectedWindows = setOf("examplePackage")
        kotlin.test.assertEquals(expectedWindows, windows)
    }

    @Test
    fun testWindowsNotAPackage() {
        val windows = mapper.collectWindowsFromView(eObjectsNotAPackage)
        kotlin.test.assertEquals(emptySet<String>(), windows)
    }

    @Test
    fun testWindowsNestedPackage() {
        val windows = mapper.collectWindowsFromView(eObjectsNestedPackage)
        val expectedWindows = setOf<String>("examplePackageNested", "nestedPackage", "examplePackage")
        kotlin.test.assertEquals(expectedWindows, windows)
    }

    /**
     * @author Patrick
     */
    @Test
    fun testMapEObjectsToWindows() {
        val preMappedWindow1 = PreMappedWindow<UmlDiagram>("examplePackage", eObjectsClassExtends.toMutableList())
        val window1 = mapper.mapEObjectsToWindows(listOf(preMappedWindow1))
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

        val classUUID = EResourceMock.getFakeUUID(getPackageAbleElement("Class1", containerPackage))
        val interfaceUUID = EResourceMock.getFakeUUID(getPackageAbleElement("Interface1", containerPackage))
        val superClassUUID = EResourceMock.getFakeUUID(getClass("Class2", containerPackage))
        val method = ((containerPackage).ownedElements[0] as Class).ownedOperations[0]
        val methodUUID = EResourceMock.getFakeUUID(method)

        val umlMethod =
            UmlMethod(methodUUID, UmlVisibility.PUBLIC, "myIntMethod", listOf(), UmlType("PrimitiveType", "int"))
        val umlInterfaceConnection =
            UmlConnection(
                classUUID + interfaceUUID,
                classUUID,
                interfaceUUID,
                UmlConnectionType.IMPLEMENTS,
                "",
                "",
                "",
            )

        val umlExtendsConnection =
            UmlConnection(
                classUUID + superClassUUID,
                classUUID,
                superClassUUID,
                UmlConnectionType.EXTENDS,
                "",
                "",
                "",
            )

        val nodes =
            listOf(
                UmlNode(
                    EResourceMock.getFakeUUID(getPackageAbleElement("Class1", container[0] as Package)),
                    "Class2",
                    "<<class>>",
                    listOf(
                        UmlAttribute(
                            getFakeUUID(getAttribute("myIntAttribute", getClass("Class1", containerPackage))),
                            UmlVisibility.PUBLIC,
                            "myIntAttribute2",
                            UmlType("PrimitiveType", "int"),
                        ),
                    ),
                    listOf(umlMethod),
                    listOf(),
                ),
                UmlNode("Class3", "Class3", "<<class>>", listOf(), listOf(umlMethod), listOf()),
                UmlNode(
                    EResourceMock.getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface2",
                    "<<interface>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
                UmlNode("Interface", "Interface1", "<<interface>>", listOf(), listOf(), listOf()),
            )

        val connections =
            listOf(
                UmlConnection("Class%Interface", "Class", "Interface", UmlConnectionType.IMPLEMENTS, "", "", ""),
                umlInterfaceConnection,
                umlExtendsConnection,
            )

        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )

        assertTrue(containerPackage.packagedElements.any { it.name == "Class3" }) // new class3
        assertTrue(containerPackage.packagedElements.any { it.name == "Interface2" }) // new interface2
        assertTrue(containerPackage.packagedElements.none { it.name == "Class1" }) // no old class1 name changed
        assertTrue(containerPackage.packagedElements.any { it.name == "Class2" }) // old class1 name changed to class2
        assertTrue(
            (containerPackage.packagedElements[0] as Class).implementedInterfaces.any {
                it.name == "Interface2"
            },
        ) // class2 implements interface2
        assertTrue(
            (containerPackage.packagedElements[0] as Class).ownedOperations.any {
                it.name == "myIntMethod"
            },
        ) // class2 has method myIntMethod
    }

    @Test
    fun testAddAbstraction() {
        val eObjectContainer = EObjectContainer()
        val container =
            eObjectContainer.getUmlContainerWith(
                listOf(eObjectContainer.getEmptyUmlClass(), eObjectContainer.getEmptyAbstractUmlClass()),
            )
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    "EmptyClass",
                    "<<class>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
                UmlNode(
                    getFakeUUID(getPackageAbleElement("EmptyAbstractClass", containerPackage)),
                    "EmptyAbstractClass",
                    "<<class>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
            )

        val connections =
            listOf<UmlConnection>(
                UmlConnection(
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)) + "$" +
                        getFakeUUID(getPackageAbleElement("EmptyAbstractClass", containerPackage)),
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    getFakeUUID(getPackageAbleElement("EmptyAbstractClass", containerPackage)),
                    UmlConnectionType.EXTENDS,
                    "",
                    "",
                    "",
                ),
            )

        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )
        assertEquals(1, (containerPackage.packagedElements[0] as Class).superClasses.size)
    }

    @Test
    fun testInvalidConnectionEdit() {
        val container = EObjectContainer().getUmlContainerWithInterfaceRealization()
        val containerPackage = container[0] as Package
        val useLessClass = UMLFactory.eINSTANCE.createClass()
        useLessClass.name = "UselessClass"
        val mutableList = container.toMutableList()
        mutableList.add(useLessClass)

        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", mutableList)
        val classUUID = EResourceMock.getFakeUUID(getPackageAbleElement("Class1", containerPackage))
        val interfaceUUID = EResourceMock.getFakeUUID(getPackageAbleElement("Interface1", containerPackage))
        val superClassUUID = EResourceMock.getFakeUUID(getClass("Class2", containerPackage))

        val invalidUmlImplementsConnection =
            UmlConnection(
                classUUID + superClassUUID,
                classUUID,
                superClassUUID,
                UmlConnectionType.IMPLEMENTS,
                "",
                "",
                "",
            )

        val invalidUmlExtendsConnection =
            UmlConnection(
                classUUID + interfaceUUID,
                classUUID,
                interfaceUUID,
                UmlConnectionType.EXTENDS,
                "",
                "",
                "",
            )

        val nodes =
            listOf(
                UmlNode(
                    EResourceMock.getFakeUUID(getPackageAbleElement("Class1", container[0] as Package)),
                    "Class2",
                    "<<class>>",
                    listOf(
                        UmlAttribute(
                            getFakeUUID(getAttribute("myIntAttribute", getClass("Class1", containerPackage))),
                            UmlVisibility.PUBLIC,
                            "myIntAttribute2",
                            UmlType("PrimitiveType", "int"),
                        ),
                    ),
                    listOf(),
                    listOf(),
                ),
                UmlNode("Class3", "Class3", "<<class>>", listOf(), listOf(), listOf()),
                UmlNode(
                    EResourceMock.getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface2",
                    "<<interface>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
                UmlNode("Interface", "Interface1", "<<interface>>", listOf(), listOf(), listOf()),
            )

        val connections =
            listOf(
                invalidUmlImplementsConnection,
                invalidUmlExtendsConnection,
            )

        val umlDiagram = UmlDiagram(nodes, connections)
        assertThrows<IllegalStateException> {
            mapper.applyWindowChangesToView(
                listOf(preMappedWindow),
                listOf(Window("examplePackage", umlDiagram)),
            )
        }
    }

    /**
     * Test if the deletion of a class, an attribute, an operation and an interface operation works.
     */
    @Test
    fun testDeleteObjects() {
        val container = EObjectContainer().getUmlContainerWithInterfaceRealization()
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Class1", containerPackage)),
                    "Class1",
                    "<<class>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface1",
                    "<<interface>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
            )

        val connections = listOf<UmlConnection>()
        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )
        assertEquals(2, containerPackage.packagedElements.size)
        assertEquals(0, getClass("Class1", containerPackage).members.size)
        assertEquals(0, getClass("Class1", containerPackage).superClasses.size)
        assertEquals(0, getClass("Class1", containerPackage).interfaceRealizations.size)
        assertEquals(0, getInterface("Interface1", containerPackage).ownedOperations.size)
        assertEquals<Class?>(null, getClassSafe("Class2", containerPackage))
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

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Class2", containerPackage)),
                    "Class2",
                    "<<class>>",
                    listOf(),
                    listOf(
                        UmlMethod(
                            getFakeUUID(getOperation("myMethod", getClass("Class2", containerPackage))),
                            UmlVisibility.PUBLIC,
                            "myMethod",
                            listOf(),
                            UmlType("PrimitiveType", "int"),
                        ),
                    ),
                    listOf(),
                ),
            )

        val connections = listOf<UmlConnection>()
        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )
        assertEquals(
            0,
            getOperation("myMethod", getClass("Class2", containerPackage))
                .ownedParameters
                .filter {
                    it.direction ==
                        ParameterDirectionKind.IN_LITERAL
                }.size,
        )
    }

    @Test
    fun testAddMethodWithParameters() {
        val eObjectContainer = EObjectContainer()
        val container = eObjectContainer.getUmlContainerWith(listOf(eObjectContainer.getEmptyUmlClass()))
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    "EmptyClass",
                    "<<class>>",
                    listOf(),
                    listOf(
                        UmlMethod(
                            "",
                            UmlVisibility.PUBLIC,
                            "myMethod",
                            listOf(
                                UmlParameter("", "myParameter", UmlType("", "int")),
                            ),
                            UmlType("", "int"),
                        ),
                    ),
                    listOf(),
                ),
            )

        val connections = listOf<UmlConnection>()
        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )
        val method = (containerPackage.packagedElements[0] as Class).ownedOperations[0]
        val parameters = method.ownedParameters.filter { it.direction == ParameterDirectionKind.IN_LITERAL }
        assertEquals(1, parameters.size)
        assertEquals("myParameter", parameters[0].name)
    }

    @Test
    fun editInterfaceMethod() {
        val eObjectContainer = EObjectContainer()
        val container = eObjectContainer.getUmlContainerWith(listOf(eObjectContainer.getSimpleUmlInterface()))
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface1",
                    "<<interface>>",
                    listOf(),
                    listOf(
                        UmlMethod(
                            getFakeUUID(getOperation("myMethod", getInterface("Interface1", containerPackage))),
                            UmlVisibility.PUBLIC,
                            "mySuperCoolInterfaceMethod",
                            listOf(),
                            UmlType("", "byte"),
                        ),
                    ),
                    listOf(),
                ),
            )

        val connections = listOf<UmlConnection>()
        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )

        val umlInterface = getInterface("Interface1", containerPackage)
        val method = getOperation("mySuperCoolInterfaceMethod", umlInterface)
        assertEquals(1, umlInterface.ownedOperations.size)
        assertEquals("mySuperCoolInterfaceMethod", method.name)
        assertEquals("byte", method.type.name)
    }

    @Test
    fun testAddOperationsAndAttributes() {
        val eObjectContainer = EObjectContainer()
        val container =
            eObjectContainer.getUmlContainerWith(
                listOf(
                    eObjectContainer.getUmlClassWithMethod(),
                    eObjectContainer.getSimpleUmlClass(),
                    eObjectContainer.getSimpleUmlInterface(),
                ),
            )
        val containerPackage = container[0] as Package

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Class2", containerPackage)),
                    "Class2",
                    "<<class>>",
                    listOf(),
                    listOf(
                        UmlMethod(
                            getFakeUUID(getOperation("myMethod", getClass("Class2", containerPackage))),
                            UmlVisibility.PUBLIC,
                            "myMethod",
                            listOf(
                                UmlParameter("", "newParameter", UmlType("PrimitiveType", "long")),
                            ),
                            UmlType("PrimitiveType", "char"),
                        ),
                    ),
                    listOf(),
                ),
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Class1", containerPackage)),
                    "Class1",
                    "<<class>>",
                    listOf(
                        UmlAttribute("", UmlVisibility.PUBLIC, "mySuperCoolAttribute", UmlType("", "int")),
                    ),
                    listOf(
                        UmlMethod("", UmlVisibility.PUBLIC, "mySuperCoolMethod", listOf(), UmlType("", "char")),
                    ),
                    listOf(),
                ),
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface1",
                    "<<interface>>",
                    listOf(),
                    listOf(
                        UmlMethod("", UmlVisibility.PUBLIC, "mySuperCoolInterfaceMethod", listOf(), UmlType("", "byte")),
                    ),
                    listOf(),
                ),
            )

        val connections = listOf<UmlConnection>()
        val umlDiagram = UmlDiagram(nodes, connections)
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )
        assertEquals(1, getClass("Class1", containerPackage).ownedOperations.size)
        assertEquals(1, getClass("Class1", containerPackage).ownedAttributes.size)
        assertEquals(1, getInterface("Interface1", containerPackage).ownedOperations.size)
        assertEquals(
            1,
            getOperation("myMethod", getClass("Class2", containerPackage))
                .ownedParameters
                .filter {
                    it.direction ==
                        ParameterDirectionKind.IN_LITERAL
                }.size,
        )
    }

    @Test
    fun testEditParameterInOperation() {
        val eObjectContainer = EObjectContainer()
        val container = eObjectContainer.getUmlContainerWith(listOf(eObjectContainer.getUmlClassWithMethod()))
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Class2", containerPackage)),
                    "Class2",
                    "<<class>>",
                    listOf(),
                    listOf(
                        UmlMethod(
                            getFakeUUID(getOperation("myMethod", getClass("Class2", containerPackage))),
                            UmlVisibility.PUBLIC,
                            "myMethod",
                            listOf(
                                UmlParameter(
                                    getFakeUUID(
                                        getParameter("myParameter", getOperation("myMethod", getClass("Class2", containerPackage))),
                                    ),
                                    "myChangedParameter",
                                    UmlType("", "newType"),
                                ),
                            ),
                            UmlType("PrimitiveType", "char"),
                        ),
                    ),
                    listOf(),
                ),
            )
        val connections = listOf<UmlConnection>()
        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )

        val parameters =
            getOperation("myMethod", getClass("Class2", containerPackage)).ownedParameters.filter {
                it.direction ==
                    ParameterDirectionKind.IN_LITERAL
            }
        assertEquals("myChangedParameter", parameters[0].name)
        assertEquals("newType", parameters[0].type.name)
    }

    @Test
    fun testCreateConnectionWithNoUUIDs() {
        val eObjectContainer = EObjectContainer()
        val container =
            eObjectContainer.getUmlContainerWith(
                listOf(eObjectContainer.getEmptyUmlClass(), eObjectContainer.getSimpleUmlInterface()),
            )
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    "EmptyClass",
                    "<<class>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface1",
                    "<<interface>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
            )

        val connections =
            listOf(
                UmlConnection(
                    "",
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    "",
                    UmlConnectionType.IMPLEMENTS,
                    "",
                    "",
                    "",
                ),
            )
        val umlDiagram = UmlDiagram(nodes, connections)

        assertThrows<IllegalStateException> {
            mapper.applyWindowChangesToView(
                listOf(preMappedWindow),
                listOf(Window("examplePackage", umlDiagram)),
            )
        }
    }

    @Test
    fun testCreateConnectionWithNotExistingTargetClass() {
        val eObjectContainer = EObjectContainer()
        val container =
            eObjectContainer.getUmlContainerWith(
                listOf(eObjectContainer.getEmptyUmlClass(), eObjectContainer.getSimpleUmlInterface()),
            )
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    "EmptyClass",
                    "<<class>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface1",
                    "<<interface>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
            )

        val connections =
            listOf(
                UmlConnection(
                    "",
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    "idontexist",
                    UmlConnectionType.IMPLEMENTS,
                    "",
                    "",
                    "",
                ),
            )
        val umlDiagram = UmlDiagram(nodes, connections)

        assertThrows<IllegalStateException> {
            mapper.applyWindowChangesToView(
                listOf(preMappedWindow),
                listOf(Window("examplePackage", umlDiagram)),
            )
        }
    }

    @Test
    fun testInterfaceGeneralizingOtherInterface() {
        val eObjectContainer = EObjectContainer()
        val container =
            eObjectContainer.getUmlContainerWith(
                listOf(
                    eObjectContainer.getSimpleUmlInterface(),
                    eObjectContainer.getEmptyUmlInterface(),
                ),
            )
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface1",
                    "<<interface>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
                UmlNode(
                    getFakeUUID(getPackageAbleElement("EmptyInterface", containerPackage)),
                    "EmptyInterface",
                    "<<interface>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
            )

        val connection =
            UmlConnection(
                getFakeUUID(getPackageAbleElement("Interface1", containerPackage)) + "$" +
                    getFakeUUID(getPackageAbleElement("EmptyInterface", containerPackage)),
                getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                getFakeUUID(getPackageAbleElement("EmptyInterface", containerPackage)),
                UmlConnectionType.EXTENDS,
                "",
                "",
                "",
            )

        val connections = listOf(connection)
        val umlDiagram = UmlDiagram(nodes, connections)
        mapper.applyWindowChangesToView(
            listOf(preMappedWindow),
            listOf(Window("examplePackage", umlDiagram)),
        )
        val umlInterface = getInterface("Interface1", containerPackage)
        val generalization = umlInterface.generalizations[0]
        assertEquals(1, umlInterface.generalizations.size)
        assertEquals("EmptyInterface", generalization.general.name)
    }

    @Test
    fun testExtendsConnectionWithClassAndInterface() {
        val eObjectContainer = EObjectContainer()
        val container =
            eObjectContainer.getUmlContainerWith(
                listOf(eObjectContainer.getEmptyUmlClass(), eObjectContainer.getSimpleUmlInterface()),
            )
        val containerPackage = container[0] as Package
        val preMappedWindow = PreMappedWindow<UmlDiagram>("examplePackage", container.toMutableList())

        val nodes =
            listOf(
                UmlNode(
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    "EmptyClass",
                    "<<class>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
                UmlNode(
                    getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    "Interface1",
                    "<<interface>>",
                    listOf(),
                    listOf(),
                    listOf(),
                ),
            )

        val connections =
            listOf(
                UmlConnection(
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)) + "$" +
                        getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    getFakeUUID(getPackageAbleElement("EmptyClass", containerPackage)),
                    getFakeUUID(getPackageAbleElement("Interface1", containerPackage)),
                    UmlConnectionType.EXTENDS,
                    "",
                    "",
                    "",
                ),
            )
        val umlDiagram = UmlDiagram(nodes, connections)
        assertThrows<IllegalStateException> {
            mapper.applyWindowChangesToView(
                listOf(preMappedWindow),
                listOf(Window("examplePackage", umlDiagram)),
            )
        }
    }

    @Test
    fun testDisplayContent() {
        assertEquals(VisualizerType.UML_VISUALIZER.visualizerName, mapper.getDisplayContentMapper().getVisualizerName())
    }

    private fun getFakeUUID(eObject: EObject): String = EResourceMock.getFakeUUID(eObject)

    private fun getPackageAbleElement(
        packageAbleElementName: String,
        umlPackage: Package,
    ): PackageableElement =
        umlPackage.packagedElements.find {
            (it is Class || it is Interface) && it.name == packageAbleElementName
        }!!

    private fun getClass(
        className: String,
        umlPackage: Package,
    ): Class =
        umlPackage.packagedElements.find {
            it is Class && it.name == className
        } as Class

    private fun getClassSafe(
        className: String,
        umlPackage: Package,
    ): Class? =
        umlPackage.packagedElements.find {
            it is Class && it.name == className
        } as Class?

    private fun getInterface(
        interfaceName: String,
        umlPackage: Package,
    ): Interface =
        umlPackage.packagedElements.find {
            it is Interface && it.name == interfaceName
        } as Interface

    private fun getAttribute(
        propertyName: String,
        umlClass: Class,
    ): Property =
        umlClass.members.find {
            it is Property && it.name == propertyName
        } as Property

    private fun getOperation(
        operationName: String,
        umlClass: Class,
    ): Operation =
        umlClass.members.find {
            it is Operation && it.name == operationName
        } as Operation

    private fun getOperation(
        operationName: String,
        umlInterface: Interface,
    ): Operation =
        umlInterface.ownedOperations.find {
            it.name == operationName
        } as Operation

    private fun getParameter(
        parameterName: String,
        umlOperation: Operation,
    ): Parameter =
        umlOperation.ownedParameters.find {
            it.name == parameterName
        } as Parameter
}
