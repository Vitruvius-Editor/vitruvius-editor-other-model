package tools.vitruv.vitruvAdapter.vitruv.impl

import tools.vitruv.vitruvAdapter.vitruv.api.ContentSelector
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.Selector
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper


/**
 * A generic display view. This class is used to create a display view with the given parameters.
 */

class GenericDisplayView(
    override val name: String,
    override val viewTypeName: String,
    override val viewMapper: ViewMapper<Any?>,
    override val windowSelector: Selector,
    override val contentSelector: ContentSelector
) : DisplayView

