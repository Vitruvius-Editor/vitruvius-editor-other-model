package tools.vitruv.vitruvAdapter.core.impl

import org.eclipse.emf.ecore.EObject

class EUtils private constructor() {

    companion object {

        fun getUUIDForEObject(eObject: EObject): String {
            return eObject.eResource().getURIFragment(eObject)
        }



    }
}