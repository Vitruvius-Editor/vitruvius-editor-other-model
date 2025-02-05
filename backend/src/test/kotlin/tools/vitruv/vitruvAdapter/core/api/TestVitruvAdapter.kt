package tools.vitruv.vitruvAdapter.core.api

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.vitruvAdapter.core.impl.DefaultDisplayViewRepositoryFactory
import tools.vitruv.vitruvAdapter.core.impl.DisplayViewRepository
import tools.vitruv.vitruvAdapter.core.impl.GenericDisplayView
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableContentSelector
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableViewMapper
import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.UmlDisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.core.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeContentSelector
import tools.vitruv.vitruvAdapter.core.impl.uml.*
import tools.vitruv.vitruvAdapter.core.impl.umlClassView.ClassDiagramContentSelector
import tools.vitruv.vitruvAdapter.core.impl.umlClassView.ClassDiagramViewMapper
import java.nio.file.Path


class TestVitruvAdapter {
    lateinit var adapter: VitruvAdapter
    lateinit var displayViewRepository: DisplayViewRepository

    @BeforeEach
    fun initVitruvAdapter() {
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("*", UMLResourceFactoryImpl())

        val vitruvClient: VitruvClient = VitruvClientFactory.create("localhost", 8000, Path.of("tools/vitruv/vitruvAdapter/tmp"))

        adapter = VitruvAdapter()


        val contentSelector = SourceCodeContentSelector()
        displayViewRepository = DefaultDisplayViewRepositoryFactory().createDisplayViewRepository()
        val sourceCodeDisplayView = GenericDisplayView("SourceCode", "UML", SourceCodeViewMapper() as ViewMapper<Any?>, AllSelector(),
            contentSelector as ContentSelector<Any?>)
        val classTableContentSelector = ClassTableContentSelector()
        val classTableDisplayView = GenericDisplayView("ClassTable", "UML", ClassTableViewMapper() as ViewMapper<Any?>, AllSelector(),
            classTableContentSelector as ContentSelector<Any?>)
        val classDiagramView = GenericDisplayView("ClassDiagram", "UML", ClassDiagramViewMapper() as ViewMapper<Any?>, AllSelector(),
            ClassDiagramContentSelector() as ContentSelector<Any?>)
        displayViewRepository.registerDisplayViews(setOf(sourceCodeDisplayView, classTableDisplayView, classDiagramView))
        adapter.setDisplayViewContainer(displayViewRepository)
        adapter.connectClient(vitruvClient)
    }

    @Test
    fun testGetDisplayViews() {
        val displayViews = adapter.getDisplayViews()
        assertNotNull(displayViews)
    }

    @Test
    fun testClassDiagram(){
        val displayView = displayViewRepository.getDisplayView("ClassDiagram")!!
        val jsonViewInformation = JsonViewInformation(displayView.viewMapper.getDisplayContent())

        //umll
        val umlNodes = listOf(
            UmlNode("", "Class1", "<<class>>",
                listOf(UmlAttribute(UmlVisibility.PUBLIC, "attribute1", "String")), listOf(), listOf()),
            UmlNode("", "Class2", "<<class>>",
                listOf(UmlAttribute(UmlVisibility.PUBLIC, "attribute2", "int")), listOf(), listOf())
        )
        val umlConnections = listOf<UmlConnection>()
        val testUmlDiagram = UmlDiagram(umlNodes, umlConnections)
        val window = Window("examplePackage2", testUmlDiagram)

        val json = jsonViewInformation.parseWindowsToJson(listOf(window as Window<Any?>))
        val newContentString = adapter.editDisplayViewAndReturnNewContent(displayView, json)
        println(newContentString)

        val secondUmlNodes = listOf(
            UmlNode("/0/Class1", "Class1", "<<class>>",
                listOf(UmlAttribute(UmlVisibility.PUBLIC, "attribute1", "String")), listOf(), listOf()),
            UmlNode("/0/Class2", "Class2", "<<class>>",
                listOf(UmlAttribute(UmlVisibility.PUBLIC, "attribute2", "int")), listOf(), listOf())
        )
        val secondUmlConnections = listOf(
            UmlConnection("", "/0/Class1", "/0/Class2", UmlConnectionType.EXTENDS, "", "", ""))
        val secondTestUmlDiagram = UmlDiagram(secondUmlNodes, secondUmlConnections)
        val secondWindow = Window("examplePackage2", secondTestUmlDiagram)

        val json2 = jsonViewInformation.parseWindowsToJson(listOf(secondWindow as Window<Any?>))
        val newContentString2 = adapter.editDisplayViewAndReturnNewContent(displayView, json2)

        println(newContentString2)
    }

//    @Test
//    fun testGetWindows(){
//        val displayView = displayViewRepository.getDisplayView("ClassTable")!!
//        val windows = adapter.getWindows(displayView)
//        println(windows)
//        val content = adapter.createWindowContent(displayView, windows)
//        print(content)
//    }
}