package tools.vitruv.vitruvAdapter.logic.api.displayViews

import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.logic.api.contents.Content

open class RemoteBasedDisplayView(var viewType: RemoteViewType, var viewMapper: ViewMapper): DisplayView {
    override fun getViewType(): ViewType<out ViewSelector> {
        TODO("Not yet implemented")
    }

    override fun getViewMapper(): ViewMapper {
        TODO("Not yet implemented")
    }

    override fun getViewContent(): Content {
        TODO("Not yet implemented")
    }
}