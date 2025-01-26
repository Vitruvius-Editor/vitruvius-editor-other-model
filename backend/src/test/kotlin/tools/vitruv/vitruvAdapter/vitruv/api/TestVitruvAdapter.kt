package tools.vitruv.vitruvAdapter.vitruv.api

import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.uml2.uml.*
import org.eclipse.uml2.uml.internal.impl.LiteralIntegerImpl
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.vitruvAdapter.vitruv.impl.DefaultDisplayViewRepositoryFactory
import tools.vitruv.vitruvAdapter.vitruv.impl.DisplayViewRepository
import tools.vitruv.vitruvAdapter.vitruv.impl.GenericDisplayView
import tools.vitruv.vitruvAdapter.vitruv.impl.mapper.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.SourceCodeContentSelector
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
            contentSelector)

        adapter.connectClient(vitruvClient)
        displayViewRepository.registerDisplayView(sourceCodeDisplayView)
        adapter.setDisplayViewContainer(displayViewRepository)
    }

    @Test
    fun testGetDisplayViews() {
        val displayViews = adapter.getDisplayViews()
        assertNotNull(displayViews)
    }

    @Test
    fun testGetWindows(){
        val displayView = displayViewRepository.getDisplayView("SourceCode")!!
        val windows = adapter.getWindows(displayView)
        println(windows)
        adapter.createWindowContent(displayView, windows)


    }


}