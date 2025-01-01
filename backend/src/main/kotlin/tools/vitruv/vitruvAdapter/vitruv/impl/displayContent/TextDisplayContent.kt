package tools.vitruv.vitruvAdapter.vitruv.impl.displayContent

import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContent
import tools.vitruv.vitruvAdapter.vitruv.api.JsonViewInformation
import tools.vitruv.vitruvAdapter.vitruv.api.Window

class TextDisplayContent(
    var textContent: String,
    windows: Set<Window>,
) : DisplayContent(windows) {
    override fun createJsonViewInformation(): JsonViewInformation {
        TODO("Not yet implemented")
    }

    override fun getName() {
        TODO("Not yet implemented")
    }
}
