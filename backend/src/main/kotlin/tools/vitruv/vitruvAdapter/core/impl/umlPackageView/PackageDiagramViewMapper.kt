package tools.vitruv.vitruvAdapter.core.impl.umlPackageView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Classifier
import org.eclipse.uml2.uml.Interface
import org.eclipse.uml2.uml.Package
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.EUtils
import tools.vitruv.vitruvAdapter.core.impl.abstractMapper.UmlViewMapper
import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.UmlDisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlConnection
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlConnectionType
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlNode
import kotlin.math.E

/**
 * Represents a package diagram view mapper.
 * This class is responsible for mapping the view content of a package diagram to windows and vice versa.
 */
class PackageDiagramViewMapper: UmlViewMapper() {

    /**
     * Maps the given view content to a list of windows.
     * @param preMappedWindows the pre-mapped windows to map to windows.
     * @return The windows representing the view content.
     */
    override fun mapEObjectsToWindowsContent(preMappedWindows: List<PreMappedWindow<UmlDiagram>>): List<Window<UmlDiagram>> {
        val windows = mutableListOf<Window<UmlDiagram>>()
        for (preMappedWindow in preMappedWindows) {
            for (eObject in preMappedWindow.neededEObjects) {
                if (eObject !is Package) {
                    continue
                }
                val umlDiagram = mapPackageToUmlDiagram(eObject)
                windows.add(preMappedWindow.createWindow(umlDiagram))
            }
        }
        return windows
    }

    private fun mapPackageToUmlDiagram(umlPackage: Package): UmlDiagram {
        val nodes = mutableListOf<UmlNode>()
        val connections = mutableListOf<UmlConnection>()
        for (subPackage in umlPackage.packagedElements) {
            if (subPackage !is Package) {
                continue
            }
            nodes.add(UmlNode(EUtils.getUUIDForEObject(subPackage), subPackage.name, "<<package>>", listOf(), listOf(), listOf()))
            connections.addAll(findConnectionsForPackage(subPackage, umlPackage))
        }
        return UmlDiagram(nodes, connections)
    }

    private fun findConnectionsForPackage(subPackage: Package, supPackage: Package): Collection<UmlConnection> {
        val umlConnections = mutableListOf<UmlConnection>()
        for (packagedElement in subPackage.packagedElements) {
            if (packagedElement is Classifier) {
                for (importedPackage in packagedElement.importedPackages) {
                    if (!supPackage.packagedElements.contains(importedPackage)) {
                        continue
                    }
                    val sourceUuid = EUtils.getUUIDForEObject(subPackage)
                    val targetUuid = EUtils.getUUIDForEObject(importedPackage)
                    umlConnections.add(UmlConnection(sourceUuid + "$" +
                        targetUuid, sourceUuid, targetUuid, UmlConnectionType.IMPORT, "", "", "imports"))
                }
            }
        }
        return umlConnections
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
        val windowPairs = pairWindowsTogether(preMappedWindows, windows)
        for (item in windowPairs) {
            applyChangesToWindow(item.first, item.second)
        }
        return listOf()
    }


    private fun applyChangesToWindow(preMappedWindow: PreMappedWindow<UmlDiagram>, window: Window<UmlDiagram>) {
        return
    }


    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        val windows = mutableSetOf<String>()
        for (rootObject in rootObjects) {
            val iterator = rootObject.eAllContents()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next is Package) {
                    windows.add(next.name)
                }
            }
            if(rootObject is Package){
                windows.add(rootObject.name)
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