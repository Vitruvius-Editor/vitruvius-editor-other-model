package tools.vitruv.vitruvAdapter.core.api

import org.eclipse.emf.ecore.EObject

/**
 * The content selector selects the elements that are contained in the windows.
 */
interface ContentSelector<E> {
    /**
     * Selects the view elements that are needed for the windows.
     * @param rootObjects the root objects of the view
     * @param windows the elements to select
     * @return the pre-mapped windows, which contain the name of the window and the EObjects that are needed to map the window.
     */
    fun applySelection(
        rootObjects: List<EObject>,
        windows: Set<String>,
    ): List<PreMappedWindow<E>>

    companion object {
        /**
         * Creates a list of pre-mapped windows with the given names.
         * @param windows the names of the windows
         * @return the pre-mapped windows
         */
        fun createMutablePreMappedWindows(windows: Set<String>): List<MutablePreMappedWindow> =
            windows.map { MutablePreMappedWindow(it, mutableListOf()) }

        /**
         * Finds a pre-mapped window by its name.
         * @param preMappedWindows the pre-mapped windows
         * @param name the name of the window
         */
        fun findPreMappedWindow(
            preMappedWindows: List<MutablePreMappedWindow>,
            name: String,
        ): MutablePreMappedWindow? = preMappedWindows.find { it.name == name }
    }
}
