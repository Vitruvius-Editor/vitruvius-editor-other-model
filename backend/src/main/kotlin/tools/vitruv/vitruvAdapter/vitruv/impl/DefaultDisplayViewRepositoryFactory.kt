package tools.vitruv.vitruvAdapter.vitruv.impl

import tools.vitruv.vitruvAdapter.vitruv.api.DisplayViewRepositoryFactory

/**
 * Default implementation of a [DisplayViewRepositoryFactory]. This implementation creates a new
 * [DisplayViewRepository] with all [DisplayView] that are used by the frontend.
 */
class DefaultDisplayViewRepositoryFactory : DisplayViewRepositoryFactory() {
    override fun createDisplayViewRepository(): DisplayViewRepository {
        // Create and register all required DisplayViews here
        return DisplayViewRepository()
    }
}