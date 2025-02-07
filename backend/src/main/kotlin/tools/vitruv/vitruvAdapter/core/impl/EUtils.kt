package tools.vitruv.vitruvAdapter.core.impl

import org.eclipse.emf.ecore.EObject

/**
 * Utility class for EObjects.
 */
class EUtils private constructor() {

    companion object {

        /**
         * Returns the UUID of an EObject in its resource.
         * @param eObject The EObject.
         * @return The UUID of the EObject. If the EObject is null or has no resource, an empty string is returned.
         */
        fun getUUIDForEObject(eObject: EObject?): String {
            if (eObject == null) {
                return ""
            }

            if (eObject.eResource() == null) {
                return ""
            }
            return eObject.eResource().getURIFragment(eObject)
        }



    }
}