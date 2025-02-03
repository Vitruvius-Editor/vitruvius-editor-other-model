package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.UMLFactory
import tools.vitruv.framework.views.View
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import org.eclipse.uml2.uml.Class
import tools.mdsd.jamopp.model.java.containers.JavaRoot

class ClassTableContentSelector: ContentSelector {

    override fun applySelection(
        rootObjects: List<EObject>,
        windows: Set<String>
    ): List<EObject> {
        val rootObjectsWithClassOnly = mutableListOf<EObject>()
        for (ePackage in rootObjects) {
            if(ePackage is Package){
                val iterator = ePackage.eAllContents()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next is Package) {
                        if(windows.contains(next.name)){
                            rootObjectsWithClassOnly.add(next)
                        }
                    }
                }

                if (windows.contains(ePackage.name)){
                    rootObjectsWithClassOnly.add(ePackage)
                }
            }
            if(ePackage is JavaRoot){
                val iterator = ePackage.eAllContents()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next is JavaRoot) {
                        if(windows.contains(next.name)){
                            rootObjectsWithClassOnly.add(next)
                        }
                    }
                    if (windows.contains(ePackage.name)){
                        rootObjectsWithClassOnly.add(next)
                    }
                }
            }
        }
        return rootObjectsWithClassOnly
    }

}