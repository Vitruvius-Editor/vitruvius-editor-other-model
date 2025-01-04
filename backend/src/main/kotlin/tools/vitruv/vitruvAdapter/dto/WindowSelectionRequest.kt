package tools.vitruv.vitruvAdapter.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = WindowSelectionRequest.All::class, name = "All"),
    JsonSubTypes.Type(value = WindowSelectionRequest.Name::class, name = "Name")
)
sealed class WindowSelectionRequest() {
    data object All : WindowSelectionRequest()

    data class Name(
        val name: String,
        val exact: Boolean,
    ) : WindowSelectionRequest()
}
