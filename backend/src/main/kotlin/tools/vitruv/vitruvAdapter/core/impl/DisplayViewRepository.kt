package tools.vitruv.vitruvAdapter.core.impl

import tools.vitruv.vitruvAdapter.core.api.DisplayView
import tools.vitruv.vitruvAdapter.core.api.DisplayViewContainer

/**
 * A repository for display views. This class is used to store and manage display views.
 */

class DisplayViewRepository : DisplayViewContainer {

    private val displayViews = mutableSetOf<DisplayView>()

    override fun registerDisplayView(displayView: DisplayView) {
        displayViews.add(displayView)
    }

    override fun registerDisplayViews(displayViews: Set<DisplayView>) {
        this.displayViews.addAll(displayViews)
    }

    override fun getDisplayViews(): Set<DisplayView> {
        return displayViews.toSet()
    }

    override fun getDisplayView(name: String): DisplayView? {
        return displayViews.find { it.name == name }
    }
}
