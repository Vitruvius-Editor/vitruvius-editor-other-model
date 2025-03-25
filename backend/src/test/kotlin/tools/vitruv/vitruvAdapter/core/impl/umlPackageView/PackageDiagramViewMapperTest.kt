package tools.vitruv.vitruvAdapter.core.impl.umlPackageView

import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.impl.uml.UmlDiagram

class PackageDiagramViewMapperTest {

    val packageDiagramViewMapper = PackageDiagramViewMapper()

    private fun getUmlPackage(packageName: String): Package {
        val umlPackage = UMLFactory.eINSTANCE.createPackage()
        umlPackage.name = packageName
        return umlPackage
    }

    fun getUmlClassImportingAnotherPackage(importingPackage: Package): org.eclipse.uml2.uml.Class {
        val umlClass = UMLFactory.eINSTANCE.createClass()
        umlClass.name = "UmlClassImportingAnotherPackage"
        val packageImport = UMLFactory.eINSTANCE.createPackageImport()
        packageImport.importedPackage = importingPackage
        packageImport.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL
        umlClass.packageImports.add(packageImport)
        return umlClass
    }


    @Test
    fun testMapPackageToDiagram() {
        val packageDiagramViewMapper = PackageDiagramViewMapper()
        val umlPackage = getUmlPackage("testPackage")
        val importingPackage = getUmlPackage("importingPackage")
        umlPackage.packagedElements.add(getUmlClassImportingAnotherPackage(importingPackage))

        val supPackage = getUmlPackage("supPackage")
        supPackage.packagedElements.add(umlPackage)
        supPackage.packagedElements.add(importingPackage)
        val premappedWindow = PreMappedWindow<UmlDiagram>("supPackage", listOf(supPackage).toMutableList())
        val windows = packageDiagramViewMapper.mapEObjectsToWindows(listOf(premappedWindow))
        println(windows)
    }
}