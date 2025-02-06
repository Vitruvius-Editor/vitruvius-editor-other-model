package tools.vitruv.vitruvAdapter.core.impl

import org.eclipse.emf.ecore.EObject

class EUtils private constructor() {

    companion object {

        fun getUUIDForEObject(eObject: EObject): String {
            if (eObject.eResource() == null) {
                return ""
            }
            return eObject.eResource().getURIFragment(eObject)
        }



    }
}