package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.mapper.UmlViewMapper

class ClassDiagramViewMapper: UmlViewMapper() {
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