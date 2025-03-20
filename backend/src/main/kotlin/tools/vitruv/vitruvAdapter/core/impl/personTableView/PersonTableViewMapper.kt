package tools.vitruv.vitruvAdapter.core.impl.personTableView

import edu.kit.ipd.sdq.metamodels.persons.Person
import edu.kit.ipd.sdq.metamodels.persons.PersonRegister
import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.VisibilityKind
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.ViewMapper
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.EUtils
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableEntry
import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.TableDisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO

class PersonTableViewMapper: ViewMapper<TableDTO<PersonTableEntry>> {

    /**
     * Maps the given view content to a list of windows.
     * @param preMappedWindows the pre-mapped windows to map to windows.
     * @return The windows representing the view content.
     */
    override fun mapEObjectsToWindowsContent(preMappedWindows: List<PreMappedWindow<TableDTO<PersonTableEntry>>>): List<Window<TableDTO<PersonTableEntry>>> {
        val windows = mutableListOf<Window<TableDTO<PersonTableEntry>>>()
        for (preMappedWindow in preMappedWindows) {
            for (rootObject in preMappedWindow.neededEObjects) {
                if (rootObject !is PersonRegister) {
                    continue
                }
                val entries = mutableListOf<PersonTableEntry>()
                rootObject.persons.forEach { element ->
                    entries.add(createPersonEntryFromPerson(element))
                }
                windows.add(preMappedWindow.createWindow(TableDTO.buildTableDTO(entries, PersonTableEntry::class)))
            }
        }
        return windows.toList()
    }

    private fun createPersonEntryFromPerson(person: Person): PersonTableEntry {
        return PersonTableEntry(
            EUtils.getUUIDForEObject(person),
            person.fullName,
            person.birthday?.toString() ?: ""
        )
    }

    /**
     * Maps the given json string to view content, compares it to [preMappedWindows] and applies the changes to the eObjects of [preMappedWindows].
     * Note that no changes will be applied to the model,
     * this have to be done after this method with a View object, where the [preMappedWindows] came from.
     * @param preMappedWindows The pre-mapped windows to compare the windows to.
     * @param windows the windows to map to EObjects.
     * @return The view content.
     */
    override fun mapWindowsToEObjectsAndApplyChangesToEObjects(
        preMappedWindows: List<PreMappedWindow<TableDTO<PersonTableEntry>>>,
        windows: List<Window<TableDTO<PersonTableEntry>>>
    ): List<EObject> {
        val windowPairs = pairWindowsTogether(preMappedWindows, windows)
        for (item in windowPairs) {
            applyChangesToWindow(item.second, item.first)
        }
        return listOf() // unnecesary return value, has to be changed
    }

    private fun applyChangesToWindow(
        window: Window<TableDTO<PersonTableEntry>>,
        preMappedWindow: PreMappedWindow<TableDTO<PersonTableEntry>>,
    ) {
        for (eObject in preMappedWindow.neededEObjects) {
            val rows = window.content.rows
            for (row in rows) {
                val person = eObject.eResource()?.getEObject(row.uuid)
                if (person == null || person !is Person) {
                    continue
                }
                person.fullName = row.fullName
            }
        }
    }

    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        val windows = mutableSetOf<String>()
        for (rootObject in rootObjects) {
            if (rootObject !is PersonRegister) {
                continue
            }
            windows.add(rootObject.persons[0].fullName)
        }
        return windows
    }

    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    override fun getDisplayContent(): DisplayContentMapper<TableDTO<PersonTableEntry>> {
        return TableDisplayContentMapper.create()
    }
}