package tools.vitruv.vitruvAdapter.vitruv.impl

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayViewRepositoryFactory

/**
 * Default implementation of a [DisplayViewRepositoryFactory]. This implementation creates a new
 * [DisplayViewRepository] with all [DisplayView] that are used by the frontend.
 */
@Component
class DefaultDisplayViewRepositoryFactory : DisplayViewRepositoryFactory() {
    @Bean
    override fun createDisplayViewRepository(): DisplayViewRepository {
        // Create and register all required DisplayViews here
        return DisplayViewRepository()
    }
}