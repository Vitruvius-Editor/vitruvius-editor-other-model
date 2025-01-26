package tools.vitruv.vitruvAdapter.vitruv.api

import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.*
import org.eclipse.uml2.uml.internal.impl.LiteralIntegerImpl
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
import java.nio.file.Path


class TestVitruvAdapter {


    val vitruvClient: VitruvClient = VitruvClientFactory.create("localhost", 8000, Path.of("tools/vitruv/vitruvAdapter/tmp"))


    val adapter = VitruvAdapter()


    val contentSelector = object : ContentSelector {
        override fun applySelection(viewSelector: ViewSelector, windows: Set<String>) {
        }
    }
    var displayViewRepository = DefaultDisplayViewRepositoryFactory().createDisplayViewRepository()
    var sourceCodeDisplayView = GenericDisplayView("SourceCode", "UML", SourceCodeViewMapper() as ViewMapper<Any?>, AllSelector(),
        contentSelector)



    @BeforeEach
    fun initVitruvAdapter() {
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
        val windows = adapter.getWindows(displayViewRepository.getDisplayView("SourceCode")!!)

    }


}