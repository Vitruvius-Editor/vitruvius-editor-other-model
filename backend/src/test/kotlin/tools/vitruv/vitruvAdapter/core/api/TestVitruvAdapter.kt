package tools.vitruv.vitruvAdapter.core.api

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.spy
import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.core.impl.DefaultDisplayViewRepositoryFactory
import tools.vitruv.vitruvAdapter.core.impl.DisplayViewRepository


class TestVitruvAdapter {
    private lateinit var adapter: VitruvAdapter
    private lateinit var displayViewRepository: DisplayViewRepository
    private lateinit var vitruvClient: VitruvClient

    @BeforeEach
    fun initVitruvAdapter() {
        vitruvClient = mock(VitruvClient::class.java)
        val viewType : ViewType<out ViewSelector> = mock(ViewType::class.java)
        Mockito.`when`(viewType.name).thenReturn("UML")

        val viewTypes = setOf(viewType)
        Mockito.`when`(vitruvClient.viewTypes).thenReturn(viewTypes)

        adapter = VitruvAdapter()
        displayViewRepository = DefaultDisplayViewRepositoryFactory().createDisplayViewRepository()
    }


    @Test
    fun testConnectClient(){
        assertDoesNotThrow { adapter.connectClient(vitruvClient) }
    }
    @Test
    fun testSetDisplayViewRepository(){
        assertDoesNotThrow { adapter.setDisplayViewContainer(displayViewRepository) }
    }

    @Test
    fun testGetDisplayViews() {
        adapter.setDisplayViewContainer(displayViewRepository)
        val displayViews = adapter.getDisplayViews()
        assertEquals(3, displayViews.size)
    }

    @Test
    fun testCollectWindowsFromJson(){
        adapter.setDisplayViewContainer(displayViewRepository)
        val json = """
        {
          "visualizerName": "TestVisualizer",
          "windows": [
            { "name": "window1", "content": "content1" },
            { "name": "window2", "content": "content2" }
          ]
        }
        """.trimIndent()
        val sourceCodeDisplayView = adapter.getDisplayViews().first()
        val windows = adapter.collectWindowsFromJson(sourceCodeDisplayView, json)
        val expectedWindowsNames = setOf("window1", "window2")
        assertEquals(expectedWindowsNames, windows)
    }

}