package tools.vitruv.vitruvAdapter.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.vitruv.vitruvAdapter.dto.ConnectionCreationRequest
import tools.vitruv.vitruvAdapter.dto.ConnectionEditRequest
import tools.vitruv.vitruvAdapter.model.ConnectionDetails
import tools.vitruv.vitruvAdapter.repository.ConnectionRepository
import java.util.*

/**
 * This service handles all project interaction.
 *
 */
@Service
class ConnectionService {
    @Autowired
    lateinit var connectionRepository: ConnectionRepository

    /**
     * Get all saved projects.
     *
     * @return A list of projects.
     */
    fun getConnections(): List<ConnectionDetails> {
        TODO()
    }

    /**
     * Imports a project onto the adapter.
     *
     * @param connectionCreationRequest Data required to import a project.
     * @return The Project object.
     */
    fun importConnection(connectionCreationRequest: ConnectionCreationRequest): ConnectionDetails {
        TODO()
    }

    /**
     * Delete a project.
     *
     * @param projectId The id of the project.
     */
    fun deleteConnection(projectId: UUID) {
        TODO()
    }

    /**
     * Edit the saved data of a project.
     *
     * @param projectId The id of the project.
     * @param editRequest The content that should be edited.
     * @return The new version of the project.
     */
    fun editConnection(
        projectId: UUID,
        editRequest: ConnectionEditRequest,
    ): ConnectionDetails {
        TODO()
    }

    /**
     * Get a project by its id.
     *
     * @param projectId The id of the project.
     * @return The project.
     */
    fun getConnectionById(projectId: UUID): ConnectionDetails {
        TODO()
    }
}
