package tools.vitruv.vitruvAdapter.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.vitruv.vitruvAdapter.dto.ProjectCreationRequest
import tools.vitruv.vitruvAdapter.dto.ProjectEditRequest
import tools.vitruv.vitruvAdapter.model.Project
import tools.vitruv.vitruvAdapter.repository.ProjectRepository
import java.util.*

@Service
class ProjectService {
    @Autowired
    lateinit var projectRepository: ProjectRepository

    fun getProjects(): List<Project> {
        TODO()
    }

    fun createProject(projectCreationRequest: ProjectCreationRequest): Project {
        TODO()
    }

    fun deleteProject(projectId: UUID): Boolean {
        TODO()
    }

    fun editProject(projectId: UUID, editRequest: ProjectEditRequest): Project {
        TODO()
    }

    fun getProjectById(projectId: UUID): Project {
        TODO()
    }
}