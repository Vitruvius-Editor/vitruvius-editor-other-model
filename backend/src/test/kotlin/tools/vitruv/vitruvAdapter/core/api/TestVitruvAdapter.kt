package tools.vitruv.vitruvAdapter.core.api

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.vitruvAdapter.core.impl.DefaultDisplayViewRepositoryFactory
import tools.vitruv.vitruvAdapter.core.impl.DisplayViewRepository
import tools.vitruv.vitruvAdapter.core.impl.GenericDisplayView
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableContentSelector
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableViewMapper
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.core.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeContentSelector
import java.nio.file.Path


class TestVitruvAdapter {
    lateinit var adapter: VitruvAdapter
    lateinit var displayViewRepository: DisplayViewRepository

    @BeforeEach
    fun initVitruvAdapter() {
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("*", UMLResourceFactoryImpl())

        //val vitruvClient: VitruvClient = VitruvClientFactory.create("localhost", 8000, Path.of("tools/vitruv/vitruvAdapter/tmp"))
        val vitruvClient: VitruvClient = mock(VitruvClient::class.java)

        adapter = VitruvAdapter()


        val contentSelector = SourceCodeContentSelector()
        displayViewRepository = DefaultDisplayViewRepositoryFactory().createDisplayViewRepository()
        val sourceCodeDisplayView = GenericDisplayView("SourceCode", "UML", SourceCodeViewMapper() as ViewMapper<Any?>, AllSelector(),
            contentSelector as ContentSelector<Any?>)
        val classTableContentSelector = ClassTableContentSelector()
        val classTableDisplayView = GenericDisplayView("ClassTable", "UML", ClassTableViewMapper() as ViewMapper<Any?>, AllSelector(),
            classTableContentSelector as ContentSelector<Any?>)

        adapter.connectClient(vitruvClient)
        displayViewRepository.registerDisplayView(sourceCodeDisplayView)
        displayViewRepository.registerDisplayView(classTableDisplayView)
        adapter.setDisplayViewContainer(displayViewRepository)
    }

    @Test
    fun testGetDisplayViews() {
        val displayViews = adapter.getDisplayViews()
        assertNotNull(displayViews)
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