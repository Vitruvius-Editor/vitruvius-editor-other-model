package tools.vitruv.vitruvAdapter.repository

import org.springframework.data.repository.Repository
import tools.vitruv.vitruvAdapter.model.ConnectionDetails
import java.util.UUID

/**
 * Data repository for Projects.
 *
 */
interface ConnectionRepository : Repository<ConnectionDetails, UUID> {
    fun findByUuid(uuid: UUID): ConnectionDetails?
}
