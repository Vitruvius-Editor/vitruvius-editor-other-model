package tools.vitruv.vitruvAdapter.repository

import org.springframework.data.repository.Repository
import tools.vitruv.vitruvAdapter.model.Project
import java.util.UUID

/**
 * Data repository for Projects.
 *
 */
interface ProjectRepository : Repository<Project, UUID> {
    fun findByUuid(uuid: UUID): Project?;
}