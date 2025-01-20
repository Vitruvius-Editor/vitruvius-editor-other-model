package tools.vitruv.vitruvAdapter.repository

import org.springframework.data.jpa.repository.JpaRepository
import tools.vitruv.vitruvAdapter.model.ConnectionDetails
import java.util.*

/**
 * Data repository for Projects.
 *
 */
interface ConnectionRepository : JpaRepository<ConnectionDetails, UUID> {
    fun findByUuid(uuid: UUID): ConnectionDetails?
}
