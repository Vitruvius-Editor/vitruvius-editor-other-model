package tools.vitruv.vitruvAdapter.dto

import tools.vitruv.vitruvAdapter.core.api.DisplayView

/**
 * A response for a display view.
 * @param name The name of the display view.
 * @param viewTypeName The type of the view.
 * @param viewMapperName The name of the view mapper.
 * @param windowSelectorName The name of the window selector.
 */

data class DisplayViewResponse(
    var name: String,
    var viewTypeName: String,
    var viewMapperName: String,
    var windowSelectorName: String,
    var contentSelectorName: String,
) {
    constructor(
        displayView: DisplayView,
    ) : this(
        displayView.name,
        displayView.name,
        displayView.viewMapper.javaClass.simpleName,
        displayView.internalSelector.javaClass.simpleName,
        displayView.contentSelector.javaClass.simpleName,
    )
}
