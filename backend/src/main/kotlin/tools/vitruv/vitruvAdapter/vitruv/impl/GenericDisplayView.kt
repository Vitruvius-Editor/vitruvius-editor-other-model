package tools.vitruv.vitruvAdapter.vitruv.impl

import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.Selector
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper

class GenericDisplayView(
    private val vitruvClient: VitruvClient,
    private val correspondingViewTypeName: String,
    override val name: String,
    override val viewMapper: ViewMapper,
    override val windowSelector: Selector,
    override val contentSelector: Selector,
) : DisplayView {

    override fun getViewType(): ViewType<*>? {
        return vitruvClient.viewTypes.find { it.name == correspondingViewTypeName }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as GenericDisplayView
        return name == that.name
    }
}
