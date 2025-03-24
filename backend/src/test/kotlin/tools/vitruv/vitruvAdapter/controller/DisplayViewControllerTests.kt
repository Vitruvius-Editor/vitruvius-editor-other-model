package tools.vitruv.vitruvAdapter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import tools.vitruv.vitruvAdapter.core.api.ViewMapper
import tools.vitruv.vitruvAdapter.core.impl.GenericDisplayView
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableViewMapper
import tools.vitruv.vitruvAdapter.core.impl.selector.AllInternalSelector
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeContentSelector
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.dto.DisplayViewContentResponse
import tools.vitruv.vitruvAdapter.dto.WindowSelectionRequest
import tools.vitruv.vitruvAdapter.services.VitruviusService
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class DisplayViewControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var vitruviusService: VitruviusService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun getDisplayViews() {
        val connectionId = UUID.randomUUID()
        val displayViews =
            setOf(
                GenericDisplayView(
                    "DisplayView 1",
                    "ExampleViewType",
                    SourceCodeViewMapper() as ViewMapper<Any?>,
                    AllInternalSelector(),
                    SourceCodeContentSelector() as ContentSelector<Any?>,
                ),
                GenericDisplayView(
                    "DisplayView 2",
                    "ExampleViewType",
                    ClassTableViewMapper() as ViewMapper<Any?>,
                    AllInternalSelector(),
                    SourceCodeContentSelector() as ContentSelector<Any?>,
                ),
            )

        Mockito.`when`(vitruviusService.getDisplayViews(connectionId)).thenReturn(displayViews)

        mockMvc
            .perform(get("/api/v1/connection/$connectionId/displayViews"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(displayViews.size))
            .andExpect(jsonPath("$[0].name").value("DisplayView 1"))
            .andExpect(jsonPath("$[1].name").value("DisplayView 2"))
    }

    @Test
    fun getDisplayViewDetails() {
        val connectionId = UUID.randomUUID()
        val displayViewName = "view1"
        val displayViewContentResponse = DisplayViewContentResponse(setOf("window1", "window2"))

        Mockito.`when`(vitruviusService.getDisplayViewWindows(connectionId, displayViewName)).thenReturn(displayViewContentResponse.windows)

        mockMvc
            .perform(get("/api/v1/connection/$connectionId/displayView/$displayViewName"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.windows.length()").value(displayViewContentResponse.windows.size))
            .andExpect(jsonPath("$.windows[0]").value("window1"))
            .andExpect(jsonPath("$.windows[1]").value("window2"))
    }

    @Test
    fun getDisplayViewWindowContent() {
        val connectionId = UUID.randomUUID()
        val displayViewName = "view1"
        val windowSelectionRequest = WindowSelectionRequest(setOf("window1", "window2"))
        val content = "window content"

        Mockito.`when`(vitruviusService.getDisplayViewContent(connectionId, displayViewName, windowSelectionRequest)).thenReturn(content)

        mockMvc
            .perform(
                post("/api/v1/connection/$connectionId/displayView/$displayViewName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(windowSelectionRequest)),
            ).andExpect(status().isOk)
            .andExpect(content().string(content))
    }

    @Test
    fun editDisplayViewContent() {
        val connectionId = UUID.randomUUID()
        val displayViewName = "view1"
        val updatedContent = "new content"

        Mockito.`when`(vitruviusService.editDisplayViewContent(connectionId, displayViewName, updatedContent)).thenReturn(updatedContent)

        mockMvc
            .perform(
                put("/api/v1/connection/$connectionId/displayView/$displayViewName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatedContent),
            ).andExpect(status().isOk)
            .andExpect(content().string(updatedContent))
    }
}
