package tools.vitruv.vitruvAdapter.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tools.vitruv.vitruvAdapter.dto.DisplayViewContentResponse
import tools.vitruv.vitruvAdapter.dto.DisplayViewResponse
import tools.vitruv.vitruvAdapter.dto.WindowSelectionRequest
import tools.vitruv.vitruvAdapter.services.VitruviusService
import java.util.*

@CrossOrigin
@RestController
@RequestMapping(value = ["/api/v1"])
class DisplayViewController {
    @Autowired
    private lateinit var vitruviusService: VitruviusService

    /**
     * Returns all DisplayViews of a given connection.
     * @param connectionId The id of the connection to operate on.
     * @return A list of available DisplayViews of the connection.
     */
    @GetMapping("/connection/{connectionId}/displayViews")
    fun getDisplayViews(
        @PathVariable connectionId: UUID,
    ): ResponseEntity<Set<DisplayViewResponse>> =
        ResponseEntity.ok(
            vitruviusService
                .getDisplayViews(connectionId)
                .map {
                    DisplayViewResponse(it)
                }.toSet(),
        )

    /**
     * Returns all available Windows of a DisplayView.
     * @param connectionId The id of the connection to operate on.
     * @param displayViewName The name of the DisplayView.
     * @return A list of all available Windows of the connection.
     */
    @GetMapping("/connection/{connectionId}/displayView/{displayViewName}")
    fun getDisplayViewDetails(
        @PathVariable connectionId: UUID,
        @PathVariable displayViewName: String,
    ): ResponseEntity<DisplayViewContentResponse> =
        ResponseEntity.ok(DisplayViewContentResponse(vitruviusService.getDisplayViewWindows(connectionId, displayViewName)))

    /**
     * Returns the content of one or multiple Windows of a DisplayView.
     * @param connectionId The id of the connection to operate on.
     * @param displayViewName The name of the DisplayView.
     * @param windowSelectionRequest The request to select the Windows.
     * @return The content of the selected Windows.
     */
    @PostMapping("/connection/{connectionId}/displayView/{displayViewName}")
    fun getDisplayViewWindowContent(
        @PathVariable connectionId: UUID,
        @PathVariable displayViewName: String,
        @RequestBody windowSelectionRequest: WindowSelectionRequest,
    ): ResponseEntity<String> =
        ResponseEntity.ok(vitruviusService.getDisplayViewContent(connectionId, displayViewName, windowSelectionRequest))

    /**
     * Edits the content of a DisplayView.
     * @param connectionId The id of the connection to operate on.
     * @param displayViewName The name of the DisplayView.
     * @param updatedContent The new content of the DisplayView.
     * @return The new content of the DisplayView.
     */
    @PutMapping("/connection/{connectionId}/displayView/{displayViewName}")
    fun editDisplayViewContent(
        @PathVariable connectionId: UUID,
        @PathVariable displayViewName: String,
        @RequestBody updatedContent: String,
    ): ResponseEntity<String> = ResponseEntity.ok(vitruviusService.editDisplayViewContent(connectionId, displayViewName, updatedContent))
}
