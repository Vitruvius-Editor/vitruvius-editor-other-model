package tools.vitruv.vitruvAdapter.core.api

import org.eclipse.emf.ecore.EObject

/**
 * A utility class to create a PreMappedWindow.
 */
data class MutablePreMappedWindow(val name: String, val neededEObjects: MutableList<EObject>) {
    /**
     * Adds an EObject to the neededEObjects list.
     * @param eObject the EObject to add
     */
    fun addEObject(eObject: EObject) {
        neededEObjects.add(eObject)
    }

    /**
     * Converts this MutablePreMappedWindow to a PreMappedWindow.
     */
    fun <E> toPreMappedWindow(): PreMappedWindow<E> {
        return PreMappedWindow(name, neededEObjects)
    }
}