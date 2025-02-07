package tools.vitruv.vitruvAdapter.core.impl.classTableView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.VisibilityKind
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier
import tools.mdsd.jamopp.printer.JaMoPPPrinter

import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.ViewMapper
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.DisplayViewName
import tools.vitruv.vitruvAdapter.core.impl.ViewRecommendation

import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.TableDisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO
import java.io.ByteArrayOutputStream

/**
 * Represents a class table view mapper.
 * This class is responsible for mapping the view content of a class table to windows and vice versa.
 */
class ClassTableViewMapper : ViewMapper<TableDTO<ClassTableEntry>> {


    /**
     * Maps the given view content to a json string, which can be displayed in the graphical editor.
     * @param preMappedWindows the pre-mapped windows to map to windows.
     * @return The json string representing the view content.
     */
    override fun mapEObjectsToWindowsContent(preMappedWindows: List<PreMappedWindow<TableDTO<ClassTableEntry>>>): List<Window<TableDTO<ClassTableEntry>>> {
        val windows = mutableListOf<Window<TableDTO<ClassTableEntry>>>()
        for (preMappedWindow in preMappedWindows) {
            for (rootObject in preMappedWindow.neededEObjects) {
                if (rootObject !is Package) {
                    continue
                }
                val entries = mutableListOf<ClassTableEntry>()
                rootObject.packagedElements.forEach { element ->
                    if (element !is Class) {
                        return@forEach
                    }
                    val javaClass = getJavaClassFromUmlClass(element, preMappedWindow.neededEObjects)
                    entries.add(createClassEntryFromClass(element, javaClass))
                    windows.add(preMappedWindow.createWindow(TableDTO.buildTableDTO(entries, ClassTableEntry::class)))
                }
            }
        }
        return windows.toList()
    }


    override fun mapWindowsToEObjectsAndApplyChangesToEObjects(
        preMappedWindows: List<PreMappedWindow<TableDTO<ClassTableEntry>>>,
        windows: List<Window<TableDTO<ClassTableEntry>>>
    ): List<EObject> {
        val windowPairs = pairWindowsTogether(preMappedWindows, windows)
        for (item in windowPairs) {
            applyChangesToWindow(item.second, item.first)
        }
        return listOf() //unnecesary return value, has to be changed
    }



    private fun applyChangesToWindow(window: Window<TableDTO<ClassTableEntry>>, preMappedWindow: PreMappedWindow<TableDTO<ClassTableEntry>>) {
        for (eObject in preMappedWindow.neededEObjects) {
            val rows = window.content.rows
            for (row in rows) {
                val umlClass = eObject.eResource()?.getEObject(row.uuid) as Class
                umlClass.name = row.name
                umlClass.visibility = VisibilityKind.get(row.visibility)
            }
        }
    }

    private fun createClassEntryFromClass(umlClass: Class, javaClass: ConcreteClassifier?): ClassTableEntry {
        val uuid = umlClass.eResource()?.getURIFragment(umlClass) ?: ""
        val name = umlClass.name ?: ""
        val visibility = umlClass.visibility.literal ?: ""
        val isAbstract = umlClass.isAbstract
        val isFinal = umlClass.isFinalSpecialization
        val superClassName = umlClass.superClasses.firstOrNull()?.name ?: ""
        val interfaces = umlClass.usedInterfaces.map { it.name }
        val attributeCount = umlClass.attributes.size
        val methodCount = umlClass.operations.size
        val linesOfCode = getClassLinesOfCode(javaClass)
        val viewRecommendations = listOf(ViewRecommendation(DisplayViewName.SOURCE_CODE.viewName, name))
        return ClassTableEntry(
            uuid,
            viewRecommendations,
            name,
            visibility,
            isAbstract,
            isFinal,
            superClassName,
            interfaces,
            attributeCount,
            methodCount,
            linesOfCode
        )
    }

    private fun getJavaClassFromUmlClass(umlClass: Class, rootObjects: List<EObject>): ConcreteClassifier? {
        for(rootObject in rootObjects){
            if(rootObject is tools.mdsd.jamopp.model.java.containers.JavaRoot){
                val iterator = rootObject.eAllContents()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next is ConcreteClassifier) {
                        if(next.name == umlClass.name ){
                            return next
                        }
                    }
                }
            }
        }
        return null
    }

    private fun getClassLinesOfCode(javaClass: ConcreteClassifier?): Int {
        if (javaClass == null) {
            return 0
        }
        val outputStream = ByteArrayOutputStream()
        JaMoPPPrinter.print(javaClass, outputStream)
        val count = outputStream.toString().lines().size
        return count
    }

    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        val windows = mutableSetOf<String>()
        for (rootObject in rootObjects) {
            val iterator = rootObject.eAllContents()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next is Package) {
                    windows.add(next.name)
                }
            }
            if (rootObject is Package) {
                windows.add(rootObject.name)
            }
        }
        return windows
    }

    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    override fun getDisplayContent(): DisplayContentMapper<TableDTO<ClassTableEntry>> {
        return TableDisplayContentMapper.create<ClassTableEntry>()
    }


}