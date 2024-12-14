package tools.vitruv.vitruvAdapter.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tools.vitruv.vitruvAdapter.dto.OpenViewRequest
import tools.vitruv.vitruvAdapter.dto.ViewResponse
import tools.vitruv.vitruvAdapter.dto.ViewTypeResponse
import tools.vitruv.vitruvAdapter.services.VitruviusService

@RestController
@RequestMapping("/api/v1")
class ViewTypeController {
    @Autowired
    lateinit var vitruviusService: VitruviusService

    /**
     * This method returns a list of all available View-Types of a project.
     *
     * @param projectId The id of the project.
     * @return A list of all available views.
     */
    @GetMapping("/projects/{projectId}/viewTypes")
    fun getViewTypes(@PathVariable("projectId") projectId: String) : ResponseEntity<List<ViewTypeResponse>> {
        return ResponseEntity.ok().build()
    }

    /**
     * This method opens a new view of a viewType
     *
     * @param projectId The id of the project/
     * @param viewTypeName The name of the View-Type
     * @param openViewRequest The data about the view that is opened.
     * @return
     */
    @PostMapping("/projects/{projectId}/viewTypes/{viewTypeName}/open")
    fun openView(@PathVariable("projectId") projectId: String, @PathVariable("viewTypeName") viewTypeName: String, @RequestBody openViewRequest: OpenViewRequest): ResponseEntity<ViewResponse> {
        return ResponseEntity.ok().build()
    }

}
