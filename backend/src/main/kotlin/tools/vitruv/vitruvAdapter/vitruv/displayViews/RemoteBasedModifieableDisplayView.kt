package tools.vitruv.vitruvAdapter.vitruv.displayViews

import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.vitruvAdapter.vitruv.displayViews.mapper.ViewMapper

class RemoteBasedModifieableDisplayView(viewType: RemoteViewType, viewMapper: ViewMapper) : RemoteBasedDisplayView(viewType, viewMapper),
    tools.vitruv.vitruvAdapter.vitruv.displayViews.ModifiableDisplayView {
    override fun updateViewContent(newContent: tools.vitruv.vitruvAdapter.vitruv.contents.Content) {
        TODO("Not yet implemented")
    }
}