package tools.vitruv.vitruv_editor_adapter.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tools.vitruv.vitruv_editor_adapter.dto.ViewContentResponse
import tools.vitruv.vitruv_editor_adapter.dto.ViewResponse

/**
 * This controller handles all requests related to views.
 *
 */
@RestController
@RequestMapping("/api/v1")
class ViewController {
    /**
     * This method returns a list of all available views of a project.
     *
     * @param projectId The id of the project.
     * @return A list of all available views.
     */
    @GetMapping("/projects/{projectId}/views")
    fun getViews(@PathVariable("projectId") projectId: String) : ResponseEntity<List<ViewResponse>> {
        return ResponseEntity.ok().build()
    }

    /**
     * Returns a single view of a project.
     *
     * @param projectId The id of the project.
     * @param viewId The id of the view.
     * @return The data of the view
     */
    @GetMapping("/projects/{projectId}/views/{viewId}")
    fun getView(@PathVariable("projectId") projectId: String, @PathVariable("viewId") viewId: String): ResponseEntity<ViewResponse> {
        return ResponseEntity.ok().build()
    }

    /**
     * Returns the content of a view.
     *
     * @param projectId The id of the project.
     * @param viewId The id of the view.
     * @return The content of the view.
     */
    @GetMapping("/projects/{projectId}/views/{viewId}/content")
    fun getViewContent(@PathVariable("projectId") projectId: String, @PathVariable("viewId") viewId: String) : ResponseEntity<ViewContentResponse> {
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
    @PutMapping("/projects/{projectId}/views/{viewId}/content")
    fun editViewContent(@PathVariable("projectId") projectId: String, @PathVariable("viewId") viewId: String, @RequestBody body: ViewContentResponse): ResponseEntity<ViewContentResponse> {
        return ResponseEntity.ok().build()
    }
}