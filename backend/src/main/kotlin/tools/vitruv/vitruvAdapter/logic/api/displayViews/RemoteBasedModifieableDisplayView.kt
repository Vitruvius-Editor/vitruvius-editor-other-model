package tools.vitruv.vitruvAdapter.logic.api.displayViews

import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.vitruvAdapter.logic.api.contents.Content

class RemoteBasedModifieableDisplayView(viewType: RemoteViewType, viewMapper: ViewMapper) : RemoteBasedDisplayView(viewType, viewMapper), ModifiableDisplayView {
    override fun updateViewContent(newContent: Content) {
        TODO("Not yet implemented")
    }
}