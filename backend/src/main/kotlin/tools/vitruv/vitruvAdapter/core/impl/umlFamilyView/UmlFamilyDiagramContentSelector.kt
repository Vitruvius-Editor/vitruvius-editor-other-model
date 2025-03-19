package tools.vitruv.vitruvAdapter.core.impl.umlFamilyView

import edu.kit.ipd.sdq.metamodels.families.FamilyRegister
import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram

class UmlFamilyDiagramContentSelector: ContentSelector<UmlDiagram> {
    /**
     * Selects the view elements that are needed for the windows.
     * @param rootObjects the root objects of the view
     * @param windows the elements to select
     * @return the pre-mapped windows, which contain the name of the window and the EObjects that are needed to map the window.
     */
    override fun applySelection(rootObjects: List<EObject>, windows: Set<String>): List<PreMappedWindow<UmlDiagram>> {
        val preMappedWindows = ContentSelector.createMutablePreMappedWindows(windows)
        for (window in windows) {
            for (rootObject in rootObjects) {
                if (rootObject !is FamilyRegister) {
                    continue
                }
                for (family in rootObject.families) {
                    if (family.lastName == window) {
                        ContentSelector.findPreMappedWindow(preMappedWindows, family.lastName)?.addEObject(family)
                    }
                }
            }
        }
        return preMappedWindows.map { it.toPreMappedWindow() }
    }
}