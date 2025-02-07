package tools.vitruv.vitruvAdapter.repository

import org.springframework.data.jpa.repository.JpaRepository
import tools.vitruv.vitruvAdapter.model.ConnectionDetails
import java.util.*

/**
 * Data repository for Projects.
 *
 */
interface ConnectionRepository : JpaRepository<ConnectionDetails, UUID> {
    /**
     * Finds a connection by its UUID.
     * @param uuid The UUID of the connection.
     * @return The connection with the given UUID or null if no connection with the given UUID exists.
     */
    fun findByUuid(uuid: UUID): ConnectionDetails?
}
