package tools.vitruv.vitruvAdapter.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.vitruv.vitruvAdapter.dto.ProjectCreationRequest
import tools.vitruv.vitruvAdapter.dto.ProjectEditRequest
import tools.vitruv.vitruvAdapter.model.Project
import tools.vitruv.vitruvAdapter.repository.ProjectRepository
import java.util.*

/**
 * This service handles all project interaction.
 *
 */
@Service
class ProjectService {
    @Autowired
    lateinit var projectRepository: ProjectRepository

    /**
     * Get all saved projects.
     *
     * @return A list of projects.
     */
    fun getProjects(): List<Project> {
        TODO()
    }

    /**
     * Imports a project onto the adapter.
     *
     * @param projectCreationRequest Data required to import a project.
     * @return The Project object.
     */
    fun importProject(projectCreationRequest: ProjectCreationRequest): Project {
        TODO()
    }

    /**
     * Delete a project.
     *
     * @param projectId The id of the project.
     */
    fun deleteProject(projectId: UUID) {
        TODO()
    }

    /**
     * Edit the saved data of a project.
     *
     * @param projectId The id of the project.
     * @param editRequest The content that should be edited.
     * @return The new version of the project.
     */
    fun editProject(projectId: UUID, editRequest: ProjectEditRequest): Project {
        TODO()
    }

    /**
     * Get a project by its id.
     *
     * @param projectId The id of the project.
     * @return The project.
     */
    fun getProjectById(projectId: UUID): Project {
        TODO()
    }
}