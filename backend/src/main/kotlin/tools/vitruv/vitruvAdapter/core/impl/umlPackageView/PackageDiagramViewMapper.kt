package tools.vitruv.vitruvAdapter.core.impl.umlPackageView

import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.abstractMapper.UmlViewMapper
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram

/**
 * Represents a package diagram view mapper.
 * This class is responsible for mapping the view content of a package diagram to windows and vice versa.
 */
class PackageDiagramViewMapper : UmlViewMapper() {
    /**
     * Maps the given view content to a list of windows.
     * @param preMappedWindows the pre-mapped windows to map to windows.
     * @return The windows representing the view content.
     */
    override fun mapEObjectsToWindows(preMappedWindows: List<PreMappedWindow<UmlDiagram>>): List<Window<UmlDiagram>> {
        TODO("Not yet implemented")
    }

    /**
     * Maps the given json string to view content, compares it to [preMappedWindows] and applies the changes to the eObjects of [preMappedWindows].
     * Note that no changes will be applied to the model,
     * this have to be done after this method with a View object, where the [preMappedWindows] came from.
     * @param preMappedWindows The pre-mapped windows to compare the windows to.
     * @param windows the windows to map to EObjects.
     * @return The view content.
     */
    override fun applyWindowChangesToView(
        preMappedWindows: List<PreMappedWindow<UmlDiagram>>,
        windows: List<Window<UmlDiagram>>,
    ): List<EObject> {
        TODO("Not yet implemented")
    }

    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    override fun collectWindowsFromView(rootObjects: List<EObject>): Set<String> {
        TODO("Not yet implemented")
    }

    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    override fun getDisplayContentMapper(): DisplayContentMapper<UmlDiagram> {
        TODO("Not yet implemented")
    }
}
