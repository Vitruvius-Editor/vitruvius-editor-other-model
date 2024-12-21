package tools.vitruv.vitruvAdapter.vitruv.impl

import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContent
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper

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

    override fun getViewContent(): DisplayContent {
        TODO("Not yet implemented")
    }
}