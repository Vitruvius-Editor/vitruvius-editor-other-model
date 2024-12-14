package tools.vitruv.vitruvAdapter.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.vitruvAdapter.dto.ViewContentResponse
import tools.vitruv.vitruvAdapter.dto.ViewResponse
import tools.vitruv.vitruvAdapter.services.VitruviusService

/**
 * This controller handles all requests related to views.
 *
 */
@RestController
@RequestMapping("/api/v1")
class ViewController {
    @Autowired
    lateinit var vitruviusService: VitruviusService

    /**
     * Returns all opened views of a project.
     *
     * @param projectId The id of the project.
     * @return A list of all opened views.
     */
    @GetMapping("/projects/{projectId}/views")
    fun getOpenedViews(@PathVariable("projectId") projectId: String): ResponseEntity<List<ViewResponse>> {
        return ResponseEntity.ok().build()
    }
    /**
     * Returns the content of a view.
     *
     * @param viewId The id of the view.
     * @return The content of the view.
     */
    @GetMapping("/view/{viewId}")
    fun getViewContent(@PathVariable("viewId") viewId: String) : ResponseEntity<ViewContentResponse> {
        return ResponseEntity.ok().build()
    }

    /**
     * Changes the content of a view.
     *
     * @param projectId The id of the project.
     * @param viewId The id of the view.
     * @param body The changes to the view.
     * @return The new content.
     */
    @PutMapping("/view/{viewId}")
    fun editViewContent(projectId: String, @PathVariable("viewId") viewId: String, @RequestBody body: ViewContentResponse): ResponseEntity<ViewContentResponse> {
        return ResponseEntity.ok().build()
    }

    /**
     * Close an opened view.
     *
     * @param viewId The id of the view that should be closed.
     * @return
     */
    @PostMapping("/view/{viewId}/close")
    fun closeView(@PathVariable viewId: String) : ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}