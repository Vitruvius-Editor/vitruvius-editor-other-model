package tools.vitruv.vitruvAdapter.vitruv.api

class JsonViewInformation(
    val displayContentName: String,
    val windows: List<Window>,
) {
    fun toJson(): String {
        // Example
        val firstLine = "name = $displayContentName"
        val content = mutableListOf<String>()
        for (window in windows) {
            content.add(window.getContent())
        }
        return StringBuilder().append(firstLine).append(content).toString()
    }
}
