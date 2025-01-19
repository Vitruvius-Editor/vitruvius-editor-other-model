package tools.vitruv.vitruvAdapter.vitruv.impl

import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.Selector
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper


class GenericDisplayView(
    override val name: String,
    override val viewTypeName: String,
    override val viewMapper: ViewMapper<Any?>,
    override val windowSelector: Selector,
    override val contentSelector: Selector
) : DisplayView

