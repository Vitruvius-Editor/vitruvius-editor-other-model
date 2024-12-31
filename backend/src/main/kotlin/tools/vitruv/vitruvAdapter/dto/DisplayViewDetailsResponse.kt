package tools.vitruv.vitruvAdapter.dto

import tools.vitruv.vitruvAdapter.vitruv.api.Window

data class DisplayViewDetailsResponse(
    var name: String,
    var windows: List<Window>,
)
