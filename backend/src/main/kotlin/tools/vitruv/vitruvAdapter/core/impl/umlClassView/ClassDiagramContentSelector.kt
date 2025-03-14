package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Package
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram

/**
 * Selects the content of a class diagram.
 */
class ClassDiagramContentSelector : ContentSelector<UmlDiagram> {
    /**
     * Selects the view elements that are needed for the windows.
     * @param rootObjects the root objects of the view
     * @param windows the elements to select
     * @return the selected elements
     */
    override fun applySelection(
        rootObjects: List<EObject>,
        windows: Set<String>,
    ): List<PreMappedWindow<UmlDiagram>> {
        val mutablePreMappedWindows = ContentSelector.createMutablePreMappedWindows(windows)
        for (ePackage in rootObjects) {
            if (ePackage is Package) {
                val iterator = ePackage.eAllContents()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next is Package) {
                        if (windows.contains(next.name)) {
                            mutablePreMappedWindows.find { it.name == next.name }?.addEObject(next)
                        }
                    }
                }
                if (windows.contains(ePackage.name)) {
                    mutablePreMappedWindows.find { it.name == ePackage.name }?.addEObject(ePackage)
                }
            }
        }
        return mutablePreMappedWindows.map { it.toPreMappedWindow() }
    }
}
