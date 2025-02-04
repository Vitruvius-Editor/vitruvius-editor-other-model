package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Package
import tools.vitruv.vitruvAdapter.core.api.ContentSelector
import tools.mdsd.jamopp.model.java.containers.JavaRoot
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO

class ClassTableContentSelector: ContentSelector {

    override fun applySelection(
        rootObjects: List<EObject>,
        windows: Set<String>
    ): List<PreMappedWindow<TableDTO<ClassTableEntry>>> {
        val mutablePreMappedWindows = ContentSelector.createMutablePreMappedWindows(windows)
        for (ePackage in rootObjects) {
            if(ePackage is Package){
                val iterator = ePackage.eAllContents()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next is Package) {
                        if(windows.contains(next.name)){
                            ContentSelector.findPreMappedWindow(mutablePreMappedWindows, next.name)?.addEObject(next)
                        }
                    }
                }


                if (windows.contains(ePackage.name)){
                    ContentSelector.findPreMappedWindow(mutablePreMappedWindows, ePackage.name)?.addEObject(ePackage)

                }
            }
            if(ePackage is JavaRoot){
                val iterator = ePackage.eAllContents()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next is JavaRoot) {
                        if(windows.contains(next.name)){
                            ContentSelector.findPreMappedWindow(mutablePreMappedWindows, next.name)?.addEObject(next)

                        }
                    }
                    if (windows.contains(ePackage.name)){
                        ContentSelector.findPreMappedWindow(mutablePreMappedWindows, ePackage.name)?.addEObject(next)

                    }
                }
            }
        }
        return mutablePreMappedWindows.map { it.toPreMappedWindow() }
    }

}