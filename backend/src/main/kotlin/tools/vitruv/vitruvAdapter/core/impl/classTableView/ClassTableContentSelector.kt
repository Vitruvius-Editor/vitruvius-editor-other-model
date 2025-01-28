package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.UMLFactory
import tools.vitruv.framework.views.View
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import org.eclipse.uml2.uml.Class

class ClassTableContentSelector: ContentSelector {

    val selectedElements = mutableListOf<EObject>()

    override fun applySelection(
        view: View,
        windows: Set<String>
    ): List<EObject> {
        val rootObjects = view.rootObjects
        for (ePackage in rootObjects) {

            val iterator = ePackage.eAllContents()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if(next is Package && windows.contains(next.name)) {
                    getClassesForPackage(next)
                    selectedElements.add(next)
                }
            }
            if(ePackage is Package && windows.contains(ePackage.name)){
                getClassesForPackage(ePackage)
                selectedElements.add(ePackage)
            }

        }
        return selectedElements
    }


   private fun getClassesForPackage(ePackage: Package) {
        for(element in ePackage.packagedElements){
            if(element !is Class){
                ePackage.packagedElements.remove(element)
            }
        }
    }
}