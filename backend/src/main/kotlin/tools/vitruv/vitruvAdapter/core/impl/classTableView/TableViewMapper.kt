package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.ViewMapper
import tools.vitruv.vitruvAdapter.core.api.Window

class TableViewMapper: ViewMapper<String> {
    override fun mapEObjectsToWindowsContent(rootObjects: List<EObject>): List<Window<String>> {
        TODO("Not yet implemented")
    }

    override fun mapWindowsContentToEObjects(windows: List<Window<String>>): List<EObject> {
        TODO("Not yet implemented")
    }

    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        TODO("Not yet implemented")
    }

    override fun getDisplayContent(): DisplayContentMapper<String> {
        TODO("Not yet implemented")
    }


}