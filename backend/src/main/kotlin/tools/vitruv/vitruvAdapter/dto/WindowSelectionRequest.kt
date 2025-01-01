package tools.vitruv.vitruvAdapter.dto

sealed class WindowSelectionRequest {
    data object All : WindowSelectionRequest()

    class Name(
        val name: String,
        val exact: Boolean,
    ) : WindowSelectionRequest()
}
