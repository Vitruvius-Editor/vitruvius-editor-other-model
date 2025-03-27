package tools.vitruv.vitruvAdapter.core.impl

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import tools.vitruv.vitruvAdapter.core.api.DisplayViewRepositoryFactory
import tools.vitruv.vitruvAdapter.core.api.ViewMapper
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableContentSelector
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableViewMapper
import tools.vitruv.vitruvAdapter.core.impl.personTableView.PersonTableContentSelector
import tools.vitruv.vitruvAdapter.core.impl.personTableView.PersonTableViewMapper
import tools.vitruv.vitruvAdapter.core.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeContentSelector
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.core.impl.umlClassView.ClassDiagramViewMapper
import tools.vitruv.vitruvAdapter.core.impl.umlFamilyView.UmlFamilyDiagramContentSelector
import tools.vitruv.vitruvAdapter.core.impl.umlFamilyView.UmlFamilyDiagramViewMapper

/**
 * Default implementation of a [DisplayViewRepositoryFactory]. This implementation creates a new
 * [DisplayViewRepository] with all [GenericDisplayView] that are used by the frontend.
 */
@Component
class DefaultDisplayViewRepositoryFactory : DisplayViewRepositoryFactory() {
    @Bean
    @Suppress("UNCHECKED_CAST")
    override fun createDisplayViewRepository(): DisplayViewRepository {
        // Create and register all required DisplayViews here
        val displayViewRepository = DisplayViewRepository()
        displayViewRepository.registerDisplayView(
            GenericDisplayView(
                DisplayViewName.PERSON_TABLE.viewName,
                "Person",
                PersonTableViewMapper() as ViewMapper<Any?>,
                AllSelector(),
                PersonTableContentSelector() as ContentSelector<Any?>,
            ),
        )
        displayViewRepository.registerDisplayView(
            GenericDisplayView(
                DisplayViewName.FAMILY_DIAGRAM.viewName,
                "Family",
                UmlFamilyDiagramViewMapper() as ViewMapper<Any?>,
                AllSelector(),
                UmlFamilyDiagramContentSelector() as ContentSelector<Any?>
            )
        )
        return displayViewRepository
    }
}
