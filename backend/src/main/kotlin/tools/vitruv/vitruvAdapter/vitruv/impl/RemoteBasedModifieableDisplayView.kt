package tools.vitruv.vitruvAdapter.vitruv.impl

import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContent
import tools.vitruv.vitruvAdapter.vitruv.api.ModifiableDisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper

class RemoteBasedModifieableDisplayView(viewType: RemoteViewType, viewMapper: ViewMapper) : RemoteBasedDisplayView(viewType, viewMapper),
    ModifiableDisplayView {
    override fun updateViewContent(newDisplayContent: DisplayContent) {
        TODO("Not yet implemented")
    }
}