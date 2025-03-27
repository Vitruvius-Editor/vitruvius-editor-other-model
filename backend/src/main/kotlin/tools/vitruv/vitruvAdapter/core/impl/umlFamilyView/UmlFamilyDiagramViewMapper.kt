package tools.vitruv.vitruvAdapter.core.impl.umlFamilyView

import edu.kit.ipd.sdq.metamodels.families.Family
import edu.kit.ipd.sdq.metamodels.families.FamilyRegister
import edu.kit.ipd.sdq.metamodels.families.Member
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.ViewMapper
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.EUtils
import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.UmlDisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlConnection
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlConnectionType
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlNode

class UmlFamilyDiagramViewMapper: ViewMapper<UmlDiagram> {
    /**
     * Maps the given view content to a list of windows.
     * @param preMappedWindows the pre-mapped windows to map to windows.
     * @return The windows representing the view content.
     */
    override fun mapEObjectsToWindowsContent(preMappedWindows: List<PreMappedWindow<UmlDiagram>>): List<Window<UmlDiagram>> {
        val windows = mutableListOf<Window<UmlDiagram>>()
        for (preMappedWindow in preMappedWindows) {
            for (neededEObject in preMappedWindow.neededEObjects) {
                if (neededEObject !is Family) {
                    continue
                }
                val umlDiagram = createUmlDiagramFromFamily(neededEObject)
                windows.add(preMappedWindow.createWindow(umlDiagram))
            }
        }
        return windows.toList()
    }

    private fun createUmlDiagramFromFamily(family: Family): UmlDiagram {
        val nodes = mutableListOf<UmlNode>()
        nodes.add(UmlNode(EUtils.getUUIDForEObject(family.father), family.father?.firstName ?: "",
            "<<father>>",
            listOf(),
            listOf(),
            listOf()))
        nodes.add(UmlNode(EUtils.getUUIDForEObject(family.mother), family.mother?.firstName ?: "",
            "<<mother>>",
            listOf(),
            listOf(),
            listOf()))
        for (son in family.sons) {
            nodes.add(UmlNode(EUtils.getUUIDForEObject(son), son.firstName,
                "<<son>>",
                listOf(),
                listOf(),
                listOf()))
        }
        for (daughter in family.daughters) {
            nodes.add(UmlNode(EUtils.getUUIDForEObject(daughter), daughter.firstName,
                "<<daughter>>",
                listOf(),
                listOf(),
                listOf()))
        }

        val connections = mutableListOf<UmlConnection>()
        connections.addAll(createConnectionsForMembers(family.father, family.mother, family.sons))
        connections.addAll(createConnectionsForMembers(family.father, family.mother, family.daughters))
        print(connections)
        return UmlDiagram(nodes, connections)
    }

    fun createConnectionsForMembers(father: Member, mother: Member, members: EList<Member>): List<UmlConnection> {
        val connections = mutableListOf<UmlConnection>()
        for (familyMember in members) {
            connections.add(UmlConnection(EUtils.getUUIDForEObject(familyMember)+"$"+father,
                EUtils.getUUIDForEObject(familyMember), EUtils.getUUIDForEObject(father), UmlConnectionType.EXTENDS,
                "","",""))
            connections.add(UmlConnection(EUtils.getUUIDForEObject(familyMember)+"$"+mother,
                EUtils.getUUIDForEObject(familyMember), EUtils.getUUIDForEObject(mother), UmlConnectionType.EXTENDS,
                "","",""))
        }
       return connections
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
        preMappedWindows: List<PreMappedWindow<UmlDiagram>>,
        windows: List<Window<UmlDiagram>>
    ): List<EObject> {
        TODO("Not yet implemented")
    }

    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        val windows = mutableSetOf<String>()
        for (rootObject in rootObjects) {
            if (rootObject !is FamilyRegister) {
                continue
            }
            for (family in rootObject.families) {
                windows.add(family.lastName)
            }
        }
        return windows
    }

    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    override fun getDisplayContent(): DisplayContentMapper<UmlDiagram> {
        return UmlDisplayContentMapper()
    }
}