package tools.vitruv.vitruvAdapter.dto

/**
 * The response for the display view content request.
 * @param windows The windows that are available for display.
 */

data class DisplayViewContentResponse(
    var windows: Set<String>,
)
