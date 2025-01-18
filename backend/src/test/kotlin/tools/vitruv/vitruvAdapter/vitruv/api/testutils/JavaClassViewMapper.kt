package tools.vitruv.vitruvAdapter.vitruv.api.testutils

import org.eclipse.emf.ecore.EObject
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper
import tools.vitruv.vitruvAdapter.vitruv.api.Window

/**
 * A mock-up mapper that takes a list of MockJavaClass EObjects
 * and turns them into Windows with simple String content.
 */
class JavaClassViewMapper : ViewMapper<String> {

    private val displayMapper = TestTextDisplayContentMapper()

    override fun mapViewToContentData(rootObjects: List<EObject>): List<Window<String>> {
        return rootObjects.map { obj ->
            val javaClass = obj as MockJavaClass
            // For example, the content could be the source-code-like string
            val contentStr = "public class ${javaClass.className} {}"
            Window(
                name = javaClass.className,
                content = contentStr
            )
        }
    }

    override fun mapContentDataToView(windows: List<Window<String>>): List<EObject> {
        return windows.map { window ->
            // In a real scenario, you'd parse the content string to extract class name, methods, etc.
            // Here, we do a trivial approach: parse the class name from the "public class XXX {}"
            val className = window.content
                .removePrefix("public class ")
                .removeSuffix(" {}")
                .trim()

            MockJavaClass(className)
        }
    }

    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        return rootObjects
            .filterIsInstance<MockJavaClass>()
            .map { it.className }
            .toSet()
    }

    override fun getDisplayContent(): DisplayContentMapper<String> {
        return displayMapper
    }
}