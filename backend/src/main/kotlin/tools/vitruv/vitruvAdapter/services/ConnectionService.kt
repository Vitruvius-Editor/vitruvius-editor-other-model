package tools.vitruv.vitruvAdapter.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.vitruv.vitruvAdapter.dto.ConnectionCreationRequest
import tools.vitruv.vitruvAdapter.dto.ConnectionEditRequest
import tools.vitruv.vitruvAdapter.exception.ConnectionNotFoundException
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
    private lateinit var connectionRepository: ConnectionRepository

    /**
     * Get all saved projects.
     *
     * @return A list of projects.
     */
    fun getConnections(): Set<ConnectionDetails> = connectionRepository.findAll().toSet()

    /**
     * Imports a project onto the adapter.
     *
     * @param connectionCreationRequest Data required to import a project.
     * @return The Project object.
     */
    fun importConnection(connectionCreationRequest: ConnectionCreationRequest): ConnectionDetails {
        val connection = ConnectionDetails(null, connectionCreationRequest.name, connectionCreationRequest.description, connectionCreationRequest.url);
        connectionRepository.save(connection);
        return connection;
    }

    /**
     * Delete a project.
     *
     * @param connectionId The id of the project.
     */
    fun deleteConnection(connectionId: UUID) {
        val connection: ConnectionDetails = getConnectionById(connectionId);
        connectionRepository.delete(connection);
    }

    /**
     * Edit the saved data of a project.
     *
     * @param connectionId The id of the project.
     * @param editRequest The content that should be edited.
     * @return The new version of the project.
     */
    fun editConnection(
        connectionId: UUID,
        editRequest: ConnectionEditRequest,
    ): ConnectionDetails {
        var connection = getConnectionById(connectionId);
        connection.name = editRequest.name;
        connection.description = editRequest.description;
        connection.url = editRequest.url;
        connectionRepository.save(connection);
        return connection;

    }

    /**
     * Get a project by its id.
     *
     * @param connectionId The id of the project.
     * @return The project.
     */
    fun getConnectionById(connectionId: UUID): ConnectionDetails = connectionRepository.findByUuid(connectionId)?: throw ConnectionNotFoundException()
}
