package tools.vitruv.vitruvAdapter.services

import org.eclipse.emf.ecore.EObject
import org.springframework.stereotype.Service
import tools.vitruv.framework.remote.client.impl.RemoteView
import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.vitruvAdapter.model.Project

/**
 * This service handles all Vitruvius Interaction. It uses the RemoteVitruviusClient to make requests to a
 * RemoteVitruviusServer
 *
 */
@Service
class VitruviusService {
    /**
     * Returns all ViewTypes of a project.
     *
     * @param project The project of which the ViewTypes shall be returned.
     * @return A list of RemoteViewTypes.
     */
    fun getViewTypes(project: Project): List<RemoteViewType> {
        TODO()
    }

    /**
     * Returns all currently opened views of a project.
     *
     * @param project The project of which the views shall be returned.
     * @return A list of RemoteViews.
     */
    fun getOpenedViews(project: Project): List<RemoteView> {
        TODO()
    }

    /**
     * Open a view of a View-Type.
     *
     * @param viewType The View-Type of which a view should be opened.
     * @param viewName The name of the view to open.
     * @return The instantiated view. Is null if an error occurred.
     */
    fun openView(viewType: RemoteViewType, viewName: String): RemoteView? {
        TODO()
    }

    /**
     * Update a view and return the updated view back.
     *
     * @param view The view to update.
     * @param newData The new data of the view.
     * @return The updated version of the view. Null if unsuccessful.
     */
    fun updateView(view: RemoteView, newData: List<EObject>): RemoteView? {
        TODO()
    }

    /**
     * Close all opened views of a project
     *
     * @param project
     */
    fun closeAllViews(project: Project) {
        TODO()
    }

    /**
     * CLose a single view.
     *
     * @param view
     */
    fun closeView(view: RemoteView) {
        TODO()
    }
}