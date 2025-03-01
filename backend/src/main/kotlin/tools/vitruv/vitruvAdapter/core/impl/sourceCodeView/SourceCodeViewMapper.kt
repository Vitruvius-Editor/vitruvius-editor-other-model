package tools.vitruv.vitruvAdapter.core.impl.sourceCodeView

import org.eclipse.emf.ecore.EObject
import tools.mdsd.jamopp.model.java.containers.JavaRoot
import tools.mdsd.jamopp.printer.JaMoPPPrinter
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.TextDisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.abstractMapper.TextViewMapper
import java.io.ByteArrayOutputStream
import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.formatter.CodeFormatter
import org.eclipse.jface.text.Document
import org.eclipse.text.edits.TextEdit
import tools.mdsd.jamopp.model.java.classifiers.Class
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow


/**
 * This class maps java classes to a string source code representation
 * Based on the java MetaModel
 */
class SourceCodeViewMapper : TextViewMapper() {

    /**
     * Maps the given view content to a list of windows.
     * @param preMappedWindows the pre-mapped windows to map to windows.
     * @return The windows representing the view content.
     */
    override fun mapEObjectsToWindowsContent(preMappedWindows: List<PreMappedWindow<String>>): List<Window<String>> {
        val windows = mutableListOf<Window<String>>()

        for (preMappedWindow in preMappedWindows) {
            for (classifier in preMappedWindow.neededEObjects) {
                if (classifier is tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier) {
                    val outputStream = ByteArrayOutputStream()
                    JaMoPPPrinter.print(classifier, outputStream)
                    val code = outputStream.toString()
                    val formattedCode = formatJavaCode(code)
                    if (formattedCode != null) {
                        windows.add(preMappedWindow.createWindow(formattedCode))
                    } else {
                        windows.add(preMappedWindow.createWindow(code))
                    }
                }
            }
        }

        return windows.toList()
    }

    fun formatJavaCode(code: String): String? {
        // Create a CodeFormatter with default options (you can also supply a map for custom settings)
        val formatter: CodeFormatter = ToolFactory.createCodeFormatter(null)
        // Format the code as a compilation unit (use CodeFormatter.K_COMPILATION_UNIT)
        val textEdit: TextEdit? = formatter.format(
            CodeFormatter.K_COMPILATION_UNIT,
            code,
            0,
            code.length,
            0,
            null
        )

        return (if (textEdit != null) {
            val document = Document(code)
            textEdit.apply(document)
            document.get()
        } else {
            code
        })
    }


    /**
     * Maps the given json string to view content, compares it to [preMappedWindows] and applies the changes to the eObjects of [preMappedWindows].
     * Note that no changes will be applied to the model,
     * this have to be done after this method with a View object, where the [preMappedWindows] came from.
     * @param preMappedWindows The pre-mapped windows to compare the windows to.
     * @param windows the windows to map to EObjects.
     * @return The view content.
     */
    override fun mapWindowsToEObjectsAndApplyChangesToEObjects(
        preMappedWindows: List<PreMappedWindow<String>>,
        windows: List<Window<String>>
    ): List<EObject> {
        val windowPairs = pairWindowsTogether(preMappedWindows, windows)
        for (item in windowPairs) {
            applyChangesToWindow(item.first, item.second)
        }
        return listOf() //unnecessary return value, has to be changed
    }

    private fun applyChangesToWindow(preMappedWindow: PreMappedWindow<String>, window: Window<String>) {
        val root = JaMoPPJDTSingleFileParser().parse(window.name, window.content.byteInputStream()) as JavaRoot
        val javaClassifier = root.classifiersInSamePackage.find { it.name == window.name }
        if (javaClassifier == null) {
            return
        }
        val javaClass = javaClassifier as Class
        for (neededEObject in preMappedWindow.neededEObjects) {
            if (neededEObject !is Class) {
                continue
            }
            if (neededEObject.name != javaClass.name) {
                continue
            }
            for (existingMethod in neededEObject.methods) {
                val method = javaClass.methods.find { it.name == existingMethod.name }
                if (method == null) {
                    continue
                }
                existingMethod.block.statements.clear()
                existingMethod.block.statements.addAll(method.block.statements)
            }
        }
    }


    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        val windows = mutableSetOf<String>()
        for (rootObject in rootObjects) {
            if (rootObject is JavaRoot) {
                val iterator = rootObject.eAllContents()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next is tools.mdsd.jamopp.model.java.classifiers.Class || next is tools.mdsd.jamopp.model.java.classifiers.Interface || next is tools.mdsd.jamopp.model.java.classifiers.Enumeration) {
                        windows.add(next.name)
                    }
                }
            }
        }
        return windows
    }

    override fun getDisplayContent(): DisplayContentMapper<String> {
        return TextDisplayContentMapper()
    }


}