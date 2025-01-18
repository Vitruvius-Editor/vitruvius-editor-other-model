package tools.vitruv.vitruvAdapter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.framework.remote.client.impl.RemoteViewSelector
import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.framework.remote.client.impl.VitruvRemoteConnection
import tools.vitruv.framework.views.ViewTypeFactory
import tools.vitruv.vitruvAdapter.dto.DisplayViewContentResponse
import tools.vitruv.vitruvAdapter.dto.DisplayViewResponse
import tools.vitruv.vitruvAdapter.dto.WindowSelectionRequest
import tools.vitruv.vitruvAdapter.exception.ConnectionNotFoundException
import tools.vitruv.vitruvAdapter.exception.DisplayViewNotFoundException
import tools.vitruv.vitruvAdapter.services.VitruviusService
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.Selector
import tools.vitruv.vitruvAdapter.vitruv.api.Window
import tools.vitruv.vitruvAdapter.vitruv.impl.GenericDisplayView
import tools.vitruv.vitruvAdapter.vitruv.impl.mapper.ClassDiagramViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.mapper.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.mapper.TextViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.mapper.UmlViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.NameSelector
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class DisplayViewControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var vitruviusService: VitruviusService

    private lateinit var displayViews: List<DisplayView>

    private lateinit var connectionId: UUID

    @BeforeEach
    fun beforeEach() {
        val viewType = ViewTypeFactory.createIdentityMappingViewType("IdentityMappingView")
        displayViews = listOf(
            GenericDisplayView("DisplayView 1", viewType, SourceCodeViewMapper(), AllSelector(), AllSelector()),
            GenericDisplayView("DisplayView 2", viewType, ClassDiagramViewMapper(), AllSelector(), AllSelector()),
        )
        connectionId = UUID.randomUUID()
    }

    @Test
    fun testGetDisplayViews() {
       whenever(vitruviusService.getDisplayViews(any<UUID>())).thenAnswer {
           if (it.getArgument(0) as UUID == connectionId) {
               displayViews
           } else {
               throw ConnectionNotFoundException()
           }
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/connection/$connectionId/displayViews"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(displayViews.map { DisplayViewResponse(it) }))
        )

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/connection/${UUID.randomUUID()}/displayViews"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testGetDisplayViewDetails() {
        whenever(vitruviusService.getDisplayViewWindows(any<UUID>(), any<String>())).thenAnswer {
            if (it.getArgument(0) as UUID != connectionId) {
                throw ConnectionNotFoundException()
            }
            if (it.getArgument(1) as String != displayViews[0].name) {
                throw DisplayViewNotFoundException()
            }
            listOf(object : Window {
                override fun getContent(): String = "Content"
            })
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/connection/$connectionId/displayView/${displayViews[0].name}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(DisplayViewContentResponse(listOf("Content")))))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/connection/${UUID.randomUUID()}/displayView/${displayViews[0].name}"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/connection/$connectionId/displayView/Unknown"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testGetDisplayViewWindowContent() {
        whenever(vitruviusService.getDisplayViewContent(any<UUID>(), any<String>(), any<Selector>())).thenAnswer {
            if (it.getArgument(0) as UUID != connectionId) {
                throw ConnectionNotFoundException()
            }
            if (it.getArgument(1) as String != displayViews[0].name) {
                throw DisplayViewNotFoundException()
            }
            "Content"
        }

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/connection/$connectionId/displayView/${displayViews[0].name}")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(WindowSelectionRequest.All))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Content"))

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/connection/${UUID.randomUUID()}/displayView/${displayViews[0].name}")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(WindowSelectionRequest.All))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/connection/$connectionId/displayView/Unknown")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(WindowSelectionRequest.All))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testEditDisplayViewContent() {
        whenever(vitruviusService.editDisplayViewContent(any<UUID>(), any<String>(), any<String>())).thenAnswer {
            if (it.getArgument(0) as UUID != connectionId) {
                throw ConnectionNotFoundException()
            }
            if (it.getArgument(1) as String != displayViews[0].name) {
                throw DisplayViewNotFoundException()
            }
            it.getArgument(2) as String
        }

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/connection/$connectionId/displayView/${displayViews[0].name}")
            .contentType("application/json")
            .content("Updated Content")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Updated Content"))

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/connection/${UUID.randomUUID()}/displayView/${displayViews[0].name}")
            .contentType("application/json")
            .content("Updated Content")
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/connection/$connectionId/displayView/Unknown")
            .contentType("application/json")
            .content("Updated Content")
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}