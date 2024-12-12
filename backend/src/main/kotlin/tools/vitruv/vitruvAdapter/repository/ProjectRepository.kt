package tools.vitruv.vitruvAdapter.repository

import org.springframework.data.repository.Repository
import tools.vitruv.vitruvAdapter.model.Project
import java.util.UUID

/**
 * Data repository for Projects.
 *
 */
interface ProjectRepository : Repository<Project, UUID> {
    fun findByUUID(uuid: UUID): Project?;
    fun getAll(): List<Project>;
}