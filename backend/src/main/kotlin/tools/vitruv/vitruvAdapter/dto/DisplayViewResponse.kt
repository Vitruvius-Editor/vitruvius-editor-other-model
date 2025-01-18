package tools.vitruv.vitruvAdapter.dto

import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView

data class DisplayViewResponse(
    var name: String,
    var viewTypeName: String,
    var viewMapperName: String,
    var windowSelectorName: String,
    var contentSelectorName: String
) {
    constructor(displayView: DisplayView) : this(displayView.name, displayView.viewType.name, displayView.viewMapper.javaClass.name, displayView.windowSelector.javaClass.name, displayView.contentSelector.javaClass.name)
}
