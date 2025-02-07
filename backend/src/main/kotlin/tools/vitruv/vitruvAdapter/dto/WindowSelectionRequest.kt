package tools.vitruv.vitruvAdapter.dto

/**
 * A request to select windows.
 * @property windows The windows to select.
 */

data class WindowSelectionRequest(val windows: Set<String>)