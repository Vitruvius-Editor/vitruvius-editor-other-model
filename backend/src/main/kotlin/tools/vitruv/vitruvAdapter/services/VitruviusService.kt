package tools.vitruv.vitruvAdapter.services

import org.eclipse.emf.ecore.EObject
import org.springframework.stereotype.Service
import tools.vitruv.framework.remote.client.impl.RemoteView
import tools.vitruv.framework.remote.client.impl.RemoteViewType
import tools.vitruv.vitruvAdapter.model.Project

@Service
class VitruviusService {
    fun getViewTypes(project: Project): List<RemoteViewType> {
        TODO()
    }

    fun getOpenedViews(project: Project): List<RemoteView> {
        TODO()
    }

    fun openView(viewType: RemoteViewType, viewName: String): RemoteView? {
        TODO()
    }

    fun updateView(view: RemoteView, newRoot: EObject): RemoteView? {
        TODO()
    }

    fun closeView(view: RemoteView) {
        TODO()
    }
}