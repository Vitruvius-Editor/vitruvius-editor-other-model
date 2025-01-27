package tools.vitruv.vitruvAdapter.vitruv.impl.selector

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Package
import tools.vitruv.framework.views.View
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.vitruvAdapter.vitruv.api.ContentSelector

class SourceCodeContentSelector: ContentSelector {
    /**
     * Applies a selection to viewSelector.selectableElements.
     * This method should only select the content for the given windows.
     * @param viewSelector the viewSelector to select the elements within
     * @param windows the windows to select the content for
     * @return The viewSelector with the selected elements.
     */
    override fun applySelection(view: View, windows: Set<String>) : List<EObject> {
        val selectableElements = mutableListOf<Class>()
        val ePackages = view.rootObjects
        for (ePackage in ePackages) {
            if (ePackage is Package) {
                for (packagedElement in ePackage.packagedElements) {
                    if (packagedElement is Class) {
                        if (windows.contains(packagedElement.name)) {
                            selectableElements.add(packagedElement)
                        }
                    }
                }
            }
        }
        return selectableElements
    }
}