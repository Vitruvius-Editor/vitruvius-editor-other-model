package tools.vitruv.vitruvAdapter.vitruv.displayViews

import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.vitruv.displayViews.mapper.ViewMapper

open class RemoteBasedDisplayView(var remoteViewType: RemoteViewType, var remoteViewMapper: ViewMapper):
    DisplayView {
    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getViewType(): ViewType<out ViewSelector> {
        TODO("Not yet implemented")
    }

    override fun getSelector(): ViewSelector {
        TODO("Not yet implemented")
    }

    override fun getViewMapper(): ViewMapper {
        TODO("Not yet implemented")
    }

    override fun getViewContent(): tools.vitruv.vitruvAdapter.vitruv.contents.Content {
        TODO("Not yet implemented")
    }
}