package tools.vitruv.vitruvAdapter.logic.api.displayViews

import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType

/**
 * THIS CLASS IS ONLY AN IDEA AND NOT FINAL
 * This interface represents a display view, which can be displayed in the Vitruvius graphical editor.
 * @author uhsab
 */
interface DisplayView {

    fun getViewType(): ViewType<out ViewSelector>
}