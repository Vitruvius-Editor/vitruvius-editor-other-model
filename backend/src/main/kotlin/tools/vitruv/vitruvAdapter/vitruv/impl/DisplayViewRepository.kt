package tools.vitruv.vitruvAdapter.vitruv.impl

import org.springframework.stereotype.Service
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayViewContainer

@Service
class DisplayViewRepository: DisplayViewContainer {
    override fun registerDisplayView(displayView: DisplayView) {
        TODO("Not yet implemented")
    }

    override fun registerDisplayViews(displayViews: Set<DisplayView>) {
        TODO("Not yet implemented")
    }

    override fun getDisplayViews(): Set<DisplayView> {
        TODO("Not yet implemented")
    }
}