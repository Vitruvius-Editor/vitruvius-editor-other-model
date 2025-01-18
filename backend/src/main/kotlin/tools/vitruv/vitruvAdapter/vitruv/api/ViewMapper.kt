package tools.vitruv.vitruvAdapter.vitruv.api

import org.eclipse.emf.ecore.EObject

/**
 * This interface represents a view mapper, which is able to map contents of a view (RootObjects/EObjects),
 * to things that can be displayed in the graphical editor. These are represented as json contents, which the graphical editor can interpret.
 * @author
 */
interface ViewMapper<E> {

    /**
     * Maps the given view content to a json string, which can be displayed in the graphical editor.
     * @param rootObjects The view content to map.
     * @return The json string representing the view content.
     */
    fun mapViewToContentData(rootObjects: List<EObject>): List<Window<E>>

    /**
     * Maps the given json string to a view content, which can be displayed in the graphical editor.
     * @param json The json string to map.
     * @return The view content.
     */
    fun mapContentDataToView(windows: List<Window<E>>): List<EObject>

    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    fun mapViewToWindows(rootObjects: List<EObject>): Set<String>

    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    fun getDisplayContent(): DisplayContentMapper<E>
}
