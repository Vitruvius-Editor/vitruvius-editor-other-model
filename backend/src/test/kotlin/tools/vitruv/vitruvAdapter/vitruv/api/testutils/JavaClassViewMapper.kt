package tools.vitruv.vitruvAdapter.vitruv.api.testutils

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.impl.EClassImpl
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.vitruv.api.ViewMapper
import tools.vitruv.vitruvAdapter.vitruv.api.Window

/**
 * A mock-up mapper that takes a list of MockJavaClass EObjects
 * and turns them into Windows with simple String content.
 */
class JavaClassViewMapper : ViewMapper<String> {

    private val displayContentMapper = TestTextDisplayContentMapper()

    override fun mapViewToContentData(rootObjects: List<EObject>): List<Window<String>> {
        //Map EClasses to a window where the window name is the class name and the content is a string build like a java class public class <name> { here are the attributes of the class}
        return rootObjects.map {
            if (it !is EClassImpl) {
                throw IllegalArgumentException("Only EClasses are supported")
            }
            val eClass = it as EClassImpl
            val className = eClass.name
            val attributes = eClass.eAttributes.joinToString("\n") { attr -> "${attr.name}: ${attr.eType.name}" }
            Window(name = className, content = "public class $className {\n$attributes\n}")
        }
    }

    override fun mapContentDataToView(windows: List<Window<String>>): List<EObject> {
        //Map windows to EClasses where the class name is the window name and the attributes can be collected out of the string like in mapViewToContentData
        //Use EFactore.einstance.createEClass() to create EClasses, you cant call EClassImpl directly
        return windows.map {
            val className = it.name
            val attributes = it.content.lines().drop(1).dropLast(1).map { line ->
                val parts = line.split(":")
                EcoreFactory.eINSTANCE.createEAttribute().apply {
                    name = parts[0].trim()
                    eType = EcoreFactory.eINSTANCE.createEClass().apply { name = parts[1].trim() }
                }
            }
            EcoreFactory.eINSTANCE.createEClass().apply {
                name = className
                eAttributes.addAll(attributes)
            }
        }
    }

    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        //Map EClasses to window names
        return rootObjects.map { (it as EClassImpl).name }.toSet()

    }

    override fun getDisplayContent(): DisplayContentMapper<String> {
       return displayContentMapper
    }
}