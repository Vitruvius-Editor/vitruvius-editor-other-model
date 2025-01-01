package tools.vitruv.vitruvAdapter.vitruv.impl

import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.Selector
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper

class GenericDisplayView(
    override val name: String,
    override val viewType: ViewType<out ViewSelector>,
    override val viewMapper: ViewMapper,
    override val windowSelector: Selector,
    override val contentSelector: Selector,
) : DisplayView
