package tools.vitruv.vitruvAdapter.core.impl

import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import tools.vitruv.vitruvAdapter.core.api.DisplayView
import tools.vitruv.vitruvAdapter.core.api.InternalSelector
import tools.vitruv.vitruvAdapter.core.api.ViewMapper

/**
 * A generic display view. This class is used to create a display view with the given parameters.
 */

class GenericDisplayView(
    override val name: String,
    override val viewTypeName: String,
    override val viewMapper: ViewMapper<Any?>,
    override val internalSelector: InternalSelector,
    override val contentSelector: ContentSelector<Any?>,
) : DisplayView
