package tools.vitruv.vitruvAdapter.core.impl.personTableView

import edu.kit.ipd.sdq.metamodels.persons.PersonRegister
import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO

class PersonTableContentSelector: ContentSelector<TableDTO<PersonTableEntry>> {
    /**
     * Selects the view elements that are needed for the windows.
     * @param rootObjects the root objects of the view
     * @param windows the elements to select
     * @return the pre-mapped windows, which contain the name of the window and the EObjects that are needed to map the window.
     */
    override fun applySelection(
        rootObjects: List<EObject>,
        windows: Set<String>
    ): List<PreMappedWindow<TableDTO<PersonTableEntry>>> {
        val preMappedWindows = ContentSelector.createMutablePreMappedWindows(windows)
        for (window in windows) {
            for (rootObject in rootObjects) {
                if (rootObject !is PersonRegister) {
                    continue
                }
                if (rootObject.persons[0].fullName == window) {
                    ContentSelector.findPreMappedWindow(preMappedWindows, rootObject.persons[0].fullName)?.addEObject(rootObject)
                }
            }
        }
        return preMappedWindows.map { it.toPreMappedWindow() }
    }
}