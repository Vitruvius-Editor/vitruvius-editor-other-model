package tools.vitruv.vitruvAdapter.core.impl

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import tools.vitruv.vitruvAdapter.core.api.DisplayViewRepositoryFactory
import tools.vitruv.vitruvAdapter.core.api.ViewMapper
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableContentSelector
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableViewMapper
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.core.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeContentSelector
import tools.vitruv.vitruvAdapter.core.impl.umlClassView.ClassDiagramViewMapper

/**
 * Default implementation of a [DisplayViewRepositoryFactory]. This implementation creates a new
 * [DisplayViewRepository] with all [GenericDisplayView] that are used by the frontend.
 */
@Component
class DefaultDisplayViewRepositoryFactory : DisplayViewRepositoryFactory() {
    @Bean
    override fun createDisplayViewRepository(): DisplayViewRepository {
        // Create and register all required DisplayViews here
        var displayViewRepository = DisplayViewRepository()
        displayViewRepository.registerDisplayView(GenericDisplayView(DisplayViewName.SOURCE_CODE.viewName, "UML", SourceCodeViewMapper() as ViewMapper<Any?>, AllSelector(),
            SourceCodeContentSelector() as ContentSelector<Any?>
        ))
        displayViewRepository.registerDisplayView(GenericDisplayView(DisplayViewName.CLASS_TABLE.viewName, "UML", ClassTableViewMapper() as ViewMapper<Any?>, AllSelector(),
            ClassTableContentSelector() as ContentSelector<Any?>
        ))
        displayViewRepository.registerDisplayView(GenericDisplayView(DisplayViewName.CLASS_DIAGRAM.viewName, "UML", ClassDiagramViewMapper() as ViewMapper<Any?>, AllSelector(), ClassTableContentSelector() as ContentSelector<Any?>))
        return displayViewRepository;
    }
}