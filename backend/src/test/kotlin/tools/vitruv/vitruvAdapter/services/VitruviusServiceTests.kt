/*package tools.vitruv.vitruvAdapter.services

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import tools.vitruv.framework.remote.client.impl.VitruvRemoteConnection
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.vitruvAdapter.dto.WindowSelectionRequest
import tools.vitruv.vitruvAdapter.exception.ConnectionNotFoundException
import tools.vitruv.vitruvAdapter.exception.DisplayViewNotFoundException
import tools.vitruv.vitruvAdapter.exception.VitruviusConnectFailedException
import tools.vitruv.vitruvAdapter.model.ConnectionDetails
import tools.vitruv.vitruvAdapter.vitruv.api.ContentSelector
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper
import tools.vitruv.vitruvAdapter.vitruv.api.VitruvAdapter
import tools.vitruv.vitruvAdapter.vitruv.impl.DisplayViewRepository
import tools.vitruv.vitruvAdapter.vitruv.impl.GenericDisplayView
import tools.vitruv.vitruvAdapter.vitruv.impl.mapper.ClassDiagramViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.mapper.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.AllSelector
import java.util.*
import kotlin.test.assertEquals

class VitruviusServiceTests {
    @InjectMocks
    private lateinit var vitruviusService: VitruviusService

    @Mock
    private lateinit var displayViewRepository: DisplayViewRepository

    @Mock
    private lateinit var connectionService: ConnectionService

    @Mock
    private lateinit var vitruvAdapter: VitruvAdapter

    private lateinit var uuid: UUID
    private lateinit var connection: ConnectionDetails
    private lateinit var displayViews: List<DisplayView>

    @BeforeEach
    fun beforeEach() {
        uuid = UUID.randomUUID()
        connection = ConnectionDetails(uuid, "Example", "Example connection", "https://example.com")
        val contentSelector = object : ContentSelector {
            override fun applySelection(viewSelector: ViewSelector, windows: Set<String>) {
            }
        }
        displayViews = listOf(
            GenericDisplayView("DisplayView 1", "ExampleViewType", SourceCodeViewMapper() as ViewMapper<Any?>, AllSelector(), contentSelector),
            GenericDisplayView("DisplayView 2", "ExampleViewType", ClassDiagramViewMapper() as ViewMapper<Any?>, AllSelector(), contentSelector),
        )
        MockitoAnnotations.openMocks(this)
        whenever(connectionService.getConnectionById(any<UUID>())).thenAnswer {
            if (it.arguments[0] == connection.uuid) {
                connection
            } else {
                throw ConnectionNotFoundException()
            }
        }

        whenever(vitruvAdapter.connectClient(any<VitruvRemoteConnection>())).thenAnswer {  }
        whenever(vitruvAdapter.getDisplayView(any<String>())).thenAnswer { if (it.arguments[0] == displayViews[0].name) displayViews[0] else null }
    }

    @Test
    fun testGetDisplayViews() {
        whenever(vitruvAdapter.getDisplayViews()).thenReturn(displayViews.toSet())
        assertEquals(displayViews.toSet(), vitruviusService.getDisplayViews(uuid))
    }

    @Test
    fun testGetDisplayViewWindows() {
        whenever(vitruvAdapter.getWindows(any<DisplayView>())).thenAnswer { setOf("Window1", "Window2") }
        assertEquals(setOf("Window1", "Window2"), vitruviusService.getDisplayViewWindows(uuid, displayViews[0].name))
        assertThrows<DisplayViewNotFoundException> { vitruviusService.getDisplayViewWindows(uuid, displayViews[1].name) }
    }

    @Test
    fun testGetDisplayViewContent() {
        whenever(vitruvAdapter.createWindowContent(any<DisplayView>(), any<Set<String>>())).thenAnswer { "Example Content" }
        assertEquals("Example Content", vitruviusService.getDisplayViewContent(uuid, displayViews[0].name, WindowSelectionRequest(setOf("Window 1", "Window 2"))))
        assertThrows<DisplayViewNotFoundException> { vitruviusService.getDisplayViewContent(uuid, displayViews[1].name, WindowSelectionRequest(setOf("Window 1", "Window 2"))) }
    }

    @Test
    fun testEditDisplayViewContent() {
        whenever(vitruvAdapter.editDisplayView(any(), any())).then {  }
        val editedContent = "Some edited content"
        assertEquals(editedContent, vitruviusService.editDisplayViewContent(uuid, displayViews[0].name, editedContent))
        assertThrows<DisplayViewNotFoundException> { vitruviusService.editDisplayViewContent(uuid, displayViews[1].name, editedContent) }
    }

    @Test
    fun testInvalidConnectionHandling() {
        // Test if the connection uuid is invalid
        assertThrows<ConnectionNotFoundException> { vitruviusService.getDisplayViews(UUID.randomUUID()) }
        assertThrows<ConnectionNotFoundException> { vitruviusService.getDisplayViewWindows(UUID.randomUUID(), displayViews[0].name) }
        assertThrows<ConnectionNotFoundException> { vitruviusService.getDisplayViewContent(UUID.randomUUID(), displayViews[1].name, WindowSelectionRequest(setOf("Window 1", "Window 2"))) }
        assertThrows<ConnectionNotFoundException> { vitruviusService.editDisplayViewContent(UUID.randomUUID(), displayViews[0].name, "Updated Content") }

        // Test if the connection uuid is valid, but the server unreachable
        whenever(vitruvAdapter.connectClient(any())).thenAnswer { throw VitruviusConnectFailedException("Could not connect to model server.") }
        assertThrows<VitruviusConnectFailedException> { vitruviusService.getDisplayViews(uuid) }
        assertThrows<VitruviusConnectFailedException> { vitruviusService.getDisplayViewWindows(uuid, displayViews[0].name) }
        assertThrows<VitruviusConnectFailedException> { vitruviusService.getDisplayViewContent(uuid, displayViews[1].name, WindowSelectionRequest(setOf("Window 1", "Window 2"))) }
        assertThrows<VitruviusConnectFailedException> { vitruviusService.editDisplayViewContent(uuid, displayViews[0].name, "Updated Content") }
    }
}*/