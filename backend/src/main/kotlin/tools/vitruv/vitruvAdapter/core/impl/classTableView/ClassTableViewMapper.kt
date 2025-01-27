package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.OpaqueBehavior
import org.eclipse.uml2.uml.Package
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.ViewMapper
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.TableDisplayContentMapper

class ClassTableViewMapper: ViewMapper<ClassTable> {
    /**
     * Maps the given view content to a json string, which can be displayed in the graphical editor.
     * @param rootObjects The view content to map.
     * @return The json string representing the view content.
     */
    override fun mapEObjectsToWindowsContent(rootObjects: List<EObject>): List<Window<ClassTable>> {
        val windows = mutableListOf<Window<ClassTable>>()
        for (rootObject in rootObjects) {
            if (rootObject !is Package) {
                continue
            }
            val packageObject = rootObject
            val entries = mutableSetOf<ClassTableEntry>()
            packageObject.packagedElements.forEach{ element ->
                    if (element !is Class) {
                        return@forEach
                    }
                    entries.add(createClassEntryFromUmlClass(element))
            }
           val window = Window(packageObject.name, ClassTable(entries))
            windows.add(window)
        }
        return windows
    }

    private fun createClassEntryFromUmlClass(umlClass: Class): ClassTableEntry {
        val uuid = umlClass.eResource().getURIFragment(umlClass)
        val name = umlClass.name
        val visibility = umlClass.visibility.literal
        val isAbstract = umlClass.isAbstract
        val isFinal = umlClass.isFinalSpecialization
        val superClassName = umlClass.superClasses.firstOrNull()?.name ?: ""
        val interfaces = umlClass.usedInterfaces.map { it.name }
        val attributeCount = umlClass.attributes.size
        val methodCount = umlClass.operations.size
        val linesOfCode = umlClass.ownedBehaviors.filterIsInstance<OpaqueBehavior>().sumBy { it.bodies.size }
        return ClassTableEntry(uuid, name, visibility, isAbstract, isFinal, superClassName, interfaces, attributeCount, methodCount, linesOfCode)
    }

    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        //Collect all windows, which are the names of the packages
        return rootObjects.filterIsInstance<Package>().map { it.name }.toSet()
    }

    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    override fun getDisplayContent(): DisplayContentMapper<ClassTable> {
        return TableDisplayContentMapper()
    }

    /**
     * Maps the given json string to a view content, which can be displayed in the graphical editor.
     * @param json The json string to map.
     * @return The view content.
     */
    override fun mapWindowsContentToEObjects(windows: List<Window<ClassTable>>): List<EObject> {
        TODO("Not yet implemented")
    }

}