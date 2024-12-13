package tools.vitruv.vitruvAdapter.logic.api.displayViews

import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.logic.api.contents.Content

/**
 * This interface represents a view mapper, which is able to map contents of a view (RootObjects/EObjects),
 * to things that can be displayed in the graphical editor. These are represented as json contents, which the graphical editor can interpret.
 * @author
 */
interface ViewMapper {

    /**
     * Maps the given view content to a json string, which can be displayed in the graphical editor.
     * @param viewContent The view content to map.
     * @return The json string representing the view content.
     */
    fun mapViewContent(viewContent: List<EObject>): Content

    /**
     * Maps the given json string to a view content, which can be displayed in the graphical editor.
     * @param json The json string to map.
     * @return The view content.
     */
    fun mapViewContent(viewContent: Content): List<EObject>
}