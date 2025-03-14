package tools.vitruv.vitruvAdapter.core.api

import org.eclipse.emf.ecore.EObject

/**
 * A window with a name and the EObjects that are needed to map the window.
 *
 */
data class PreMappedWindow<E>(
    val name: String,
    val neededEObjects: MutableList<EObject>,
) {
    /**
     * Creates a Window with the given content from the pre-mapped window.
     * @param content the content of the window
     * @return the window
     */
    fun createWindow(content: E): Window<E> = Window(name, content)
}
