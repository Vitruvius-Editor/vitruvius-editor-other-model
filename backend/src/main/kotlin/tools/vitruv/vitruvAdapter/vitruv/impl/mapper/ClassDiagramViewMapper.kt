package tools.vitruv.vitruvAdapter.vitruv.impl.mapper

import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.vitruv.api.Window

class ClassDiagramViewMapper: UmlViewMapper() {
    override fun mapViewToContentData(rootObjects: List<EObject>): List<Window<String>> {
        TODO("Not yet implemented")
    }

    override fun mapContentDataToView(windows: List<Window<String>>): List<EObject> {
        TODO("Not yet implemented")
    }

    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        TODO("Not yet implemented")
    }

    override fun getDisplayContent(): DisplayContentMapper<String> {
        TODO("Not yet implemented")
    }
}
