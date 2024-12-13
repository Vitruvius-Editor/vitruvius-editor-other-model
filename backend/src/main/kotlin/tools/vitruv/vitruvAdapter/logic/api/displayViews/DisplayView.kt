package tools.vitruv.vitruvAdapter.logic.api.displayViews

import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType

/**
 * This interface represents a display view, which can be displayed in the Vitruvius graphical editor.
 * @author uhsab
 */
interface DisplayView {

    /**
     * Gets the relating vitruvius view type.
     * @return The relating vitruvius view type.
     */
    fun getViewType(): ViewType<out ViewSelector>

    /**
     * Gets the view mapper of the display view.
     * @return The view mapper of the display view.
     */
    fun getViewMapper(): ViewMapper

}
