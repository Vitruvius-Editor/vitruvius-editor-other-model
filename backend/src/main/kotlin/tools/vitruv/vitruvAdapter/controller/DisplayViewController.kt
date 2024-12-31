package tools.vitruv.vitruvAdapter.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tools.vitruv.vitruvAdapter.dto.DisplayViewDetailsResponse
import tools.vitruv.vitruvAdapter.dto.DisplayViewResponse
import tools.vitruv.vitruvAdapter.dto.WindowSelectionRequest
import tools.vitruv.vitruvAdapter.services.VitruviusService
import java.util.*

@RestController
@RequestMapping(value = ["/api/v1"])
class DisplayViewController {
    @Autowired
    lateinit var vitruviusService: VitruviusService

    @GetMapping("/connection/{projectId}/displayViews")
    fun getDisplayViews(
        @PathVariable projectId: UUID,
    ): ResponseEntity<List<DisplayViewResponse>> {
        TODO("Not yet implemented")
    }

    @GetMapping("/connection/{projectId}/displayView/{displayViewName}")
    fun getDisplayViewDetails(
        @PathVariable projectId: UUID,
        @PathVariable displayViewName: String,
    ): ResponseEntity<DisplayViewDetailsResponse> {
        TODO("Not yet implemented")
    }

    @PostMapping("/connection/{projectId}/displayView/{displayViewName}")
    fun getDisplayViewWindowContent(
        @PathVariable projectId: UUID,
        @PathVariable displayViewName: String,
        @RequestBody windowSelectionRequest: WindowSelectionRequest,
    ): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

    @PutMapping("/connection/{projectId}/displayView/{displayViewName}")
    fun editDisplayViewContent(
        @PathVariable projectId: UUID,
        @PathVariable displayViewName: String,
        @RequestBody updatedContent: String,
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }
}
