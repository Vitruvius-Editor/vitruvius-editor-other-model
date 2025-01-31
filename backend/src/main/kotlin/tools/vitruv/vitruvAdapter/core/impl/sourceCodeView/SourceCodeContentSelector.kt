package tools.vitruv.vitruvAdapter.core.impl.sourceCodeView

import org.eclipse.emf.ecore.EObject
import tools.mdsd.jamopp.model.java.containers.JavaRoot
import tools.vitruv.framework.views.View
import tools.vitruv.vitruvAdapter.core.api.ContentSelector

class SourceCodeContentSelector : ContentSelector {
    /**
     * Applies a selection to viewSelector.selectableElements.
     * This method should only select the content for the given windows.
     * @param viewSelector the viewSelector to select the elements within
     * @param windows the windows to select the content for
     * @return The viewSelector with the selected elements.
     */

    override fun applySelection(view: View, windows: Set<String>): List<EObject> {
        val selectedElements = mutableListOf<EObject>()
        val rootObjects = view.rootObjects
        for (rootObject in rootObjects) {
            if(rootObject is JavaRoot){
                val iterator = rootObject.eAllContents()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next is tools.mdsd.jamopp.model.java.classifiers.Class|| next is tools.mdsd.jamopp.model.java.classifiers.Interface || next is tools.mdsd.jamopp.model.java.classifiers.Enumeration) {
                        if (windows.contains(next.name)) {
                            selectedElements.add(next)
                        }
                    }
                }
            }
        }
        return selectedElements
    }

}