package tools.vitruv.vitruv_editor_adapter.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tools.vitruv.vitruv_editor_adapter.dto.*

/**
 * This rest controller handles all requests that deal with the management of projects.
 *
 */
@RestController
@RequestMapping(value = ["/api/v1"])
class ProjectController {
    /**
     * This method returns a list of all saved projects.
     *
     * @return The list of all projects.
     */
    @GetMapping("/projects")
    fun getProjects() : List<ProjectResponse> {
        return listOf()
    }

    /**
     * This method returns the data of a single project.
     *
     * @param id The id of the project.
     * @return The content of the project.
     */
    @GetMapping("/projects/{id}")
    fun getProject(@PathVariable("id") id: String) : ResponseEntity<ProjectResponse> {
        return ResponseEntity.ok(ProjectResponse("foo", "bar", id, "example.com"))
    }

    /**
     * This method creates a new project and returns its data.
     *
     * @param body Information required to create a new project.
     * @return The content of the new project.
     */
    @PostMapping("/project")
    fun createProject(@RequestBody body: ProjectCreationRequest): ResponseEntity<ProjectResponse> {
        return ResponseEntity.ok(ProjectResponse(body.name, body.description, "1337", "example.com"))
    }

    /**
     * This method deletes a project.
     *
     * @param id The id of the project to delete.
     * @return An empty return value containing the status code.
     */
    @DeleteMapping("/project/{id}")
    fun deleteProject(@PathVariable("id") id: String) : ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

    /**
     * This method edits a project.
     *
     * @param id The id of the project to edit
     * @param body The data to edit.
     * @return The new content of the project.
     */
    @PutMapping("/project/{id}")
    fun editProject(@PathVariable("id") id: String, @RequestBody body: ProjectEditRequest) : ResponseEntity<ProjectResponse> {
        return ResponseEntity.ok(ProjectResponse(body.name, body.description, "1337", body.location))
    }
}