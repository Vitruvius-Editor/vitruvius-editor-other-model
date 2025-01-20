package tools.vitruv.vitruvAdapter.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tools.vitruv.vitruvAdapter.dto.DisplayViewContentResponse
import tools.vitruv.vitruvAdapter.dto.DisplayViewResponse
import tools.vitruv.vitruvAdapter.dto.WindowSelectionRequest
import tools.vitruv.vitruvAdapter.services.VitruviusService
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.NameSelector
import java.util.*

@RestController
@RequestMapping(value = ["/api/v1"])
class DisplayViewController {
    @Autowired
    private lateinit var vitruviusService: VitruviusService

    @GetMapping("/connection/{connectionId}/displayViews")
    fun getDisplayViews(
        @PathVariable connectionId: UUID,
    ): ResponseEntity<List<DisplayViewResponse>> = ResponseEntity.ok(vitruviusService.getDisplayViews(connectionId).map { DisplayViewResponse(it) })

    @GetMapping("/connection/{connectionId}/displayView/{displayViewName}")
    fun getDisplayViewDetails(
        @PathVariable connectionId: UUID,
        @PathVariable displayViewName: String,
    ): ResponseEntity<DisplayViewContentResponse> = ResponseEntity.ok(DisplayViewContentResponse(vitruviusService.getDisplayViewWindows(connectionId, displayViewName)))

    @PostMapping("/connection/{connectionId}/displayView/{displayViewName}")
    fun getDisplayViewWindowContent(
        @PathVariable connectionId: UUID,
        @PathVariable displayViewName: String,
        @RequestBody windowSelectionRequest: WindowSelectionRequest,
    ): ResponseEntity<String> = ResponseEntity.ok(vitruviusService.getDisplayViewContent(connectionId, displayViewName, windowSelectionRequest))

    @PutMapping("/connection/{connectionId}/displayView/{displayViewName}")
    fun editDisplayViewContent(
        @PathVariable connectionId: UUID,
        @PathVariable displayViewName: String,
        @RequestBody updatedContent: String,
    ): ResponseEntity<String> = ResponseEntity.ok(vitruviusService.editDisplayViewContent(connectionId, displayViewName, updatedContent))
}
