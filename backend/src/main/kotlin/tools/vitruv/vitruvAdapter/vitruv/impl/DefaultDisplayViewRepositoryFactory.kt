package tools.vitruv.vitruvAdapter.vitruv.impl

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayViewRepositoryFactory
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.mapper.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.vitruv.impl.selector.SourceCodeContentSelector

/**
 * Default implementation of a [DisplayViewRepositoryFactory]. This implementation creates a new
 * [DisplayViewRepository] with all [DisplayView] that are used by the frontend.
 */
@Component
class DefaultDisplayViewRepositoryFactory : DisplayViewRepositoryFactory() {
    @Bean
    override fun createDisplayViewRepository(): DisplayViewRepository {
        // Create and register all required DisplayViews here
        var displayViewRepository = DisplayViewRepository()
        displayViewRepository.registerDisplayView(GenericDisplayView("SourceCode", "UML", SourceCodeViewMapper() as ViewMapper<Any?>, AllSelector(),
            SourceCodeContentSelector()
        ))
        return displayViewRepository;
    }
}