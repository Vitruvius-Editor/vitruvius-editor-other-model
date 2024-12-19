package tools.vitruv.vitruvAdapter.logic.api.displayViews

import org.eclipse.emf.ecore.EObject
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.logic.api.contents.Content

/**
 * This interface represents a display view, which can be displayed in the Vitruvius graphical editor.
 * @author uhsab
 */
interface DisplayView {

    /**
     * Gets the name of the display view.
     * @return The name of the display view.
     */
    fun getName(): String

    /**
     * Gets the relating vitruvius view type.
     * @return The relating vitruvius view type.
     */
    fun getViewType(): ViewType<out ViewSelector>

    /**
     * Gets the selector of the display view.
     * @return The selector of the display view.
     */
    fun getSelector(): ViewSelector

    /**
     * Gets the view mapper of the display view.
     * @return The view mapper of the display view.
     */
    fun getViewMapper(): ViewMapper

    /**
     * Get the content of the viewType in a processed format.
     *
     * @return
     */
    fun getViewContent(): Content
}
