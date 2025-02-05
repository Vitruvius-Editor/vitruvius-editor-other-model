package tools.vitruv.vitruvAdapter.core.api

import org.eclipse.emf.ecore.EObject

/**
 * This interface represents a view mapper, which is able to map contents of a view (RootObjects/EObjects),
 * to Windows that can be displayed in the graphical editor.
 * @author uhsab
 */
interface ViewMapper<E> {

    /**
     * Maps the given view content to a list of windows.
     * @param preMappedWindows the pre-mapped windows to map to windows.
     * @return The windows representing the view content.
     */
    fun mapEObjectsToWindowsContent(preMappedWindows: List<PreMappedWindow<E>>): List<Window<E>>

    /**
     * Maps the given json string to view content, compares it to [preMappedWindows] and applies the changes to the eObjects of [preMappedWindows].
     * Note that no changes will be applied to the model,
     * this have to be done after this method with a View object, where the [preMappedWindows] came from.
     * @param preMappedWindows The pre-mapped windows to compare the windows to.
     * @param windows the windows to map to EObjects.
     * @return The view content.
     */
    fun mapWindowsToEObjectsAndApplyChangesToEObjects(preMappedWindows: List<PreMappedWindow<E>>, windows: List<Window<E>>): List<EObject>

    /**
     * Pairs the pre-mapped windows with the windows.
     * @param preMappedWindows The pre-mapped windows.
     * @param windows The windows.
     * @return The pairs of pre-mapped windows and windows.
     */
    fun pairWindowsTogether(preMappedWindows: List<PreMappedWindow<E>>, windows: List<Window<E>>): List<Pair<PreMappedWindow<E>, Window<E>>> {
        val pairs = mutableListOf<Pair<PreMappedWindow<E>, Window<E>>>()
        for (window in windows) {
            val preMappedWindow = preMappedWindows.find { it.name == window.name }
            if (preMappedWindow != null) {
                pairs.add(Pair(preMappedWindow, window))
            }
        }
        return pairs
    }

    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    fun mapViewToWindows(rootObjects: List<EObject>): Set<String>


    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    fun getDisplayContent(): DisplayContentMapper<E>
}
