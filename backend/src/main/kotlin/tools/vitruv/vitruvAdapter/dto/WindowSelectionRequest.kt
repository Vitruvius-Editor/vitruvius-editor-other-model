package tools.vitruv.vitruvAdapter.dto

import com.fasterxml.jackson.annotation.JsonCreator

sealed class WindowSelectionRequest() {
    data object All : WindowSelectionRequest()

    data class Name(
        val name: String,
        val exact: Boolean,
    ) : WindowSelectionRequest()
}
