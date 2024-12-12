package tools.vitruv.vitruvAdapter.dto

data class ViewTypeResponse(
    val name: String,
    val views: List<ViewTypeResponse>
)