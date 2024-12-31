package tools.vitruv.vitruvAdapter.vitruv.impl.displayContent

import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContent
import tools.vitruv.vitruvAdapter.vitruv.api.JsonViewInformation

class TextDisplayContent(
    name: String,
    var textContent: String,
) : DisplayContent(name) {
    override fun createJsonViewInformation(): JsonViewInformation {
        TODO("Not yet implemented")
    }
}
