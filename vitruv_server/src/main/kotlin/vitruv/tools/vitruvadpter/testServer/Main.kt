package vitruv.tools.vitruvadpter.testServer

import org.eclipse.uml2.uml.UMLFactory

import tools.vitruv.framework.remote.client.VitruvClientFactory
import java.nio.file.Path

fun main() {
    val serverInitializer = ServerInitializer()
    val server = serverInitializer.initialize()
    server.start()
    println("Vitruvius server started on: " + serverInitializer.serverPort);

    //clientTest(serverInitializer)

    //start client

//    rootPackage.packagedElements.add(newClass)
//    umlView.commitChanges()
//    umlView.close()


}

fun clientTest(serverInitializer: ServerInitializer) {
    val client = VitruvClientFactory.create("localhost", serverInitializer.serverPort, Path.of("vitruv_server/test"))
    var clientViewTypes = client.viewTypes

    var umlViewType = clientViewTypes.stream().filter{it.name == "UML"}.findAny()

    print(umlViewType.get().name)



    var umlSelector = umlViewType.get().createSelector(null)
    umlSelector.selectableElements.forEach { it -> umlSelector.setSelected(it, true) }

    var umlView = umlSelector.createView().withChangeDerivingTrait()
    val umlPackage = umlView.rootObjects.first() as org.eclipse.uml2.uml.Package


    var factory = UMLFactory.eINSTANCE
    var newClass = factory.createClass()
    newClass.name = "NewClass"
    umlPackage.packagedElements.add(newClass)
    umlView.commitChangesAndUpdate()

    print(umlPackage.packagedElements)
    val umlPackage1 = umlView.rootObjects.first() as org.eclipse.uml2.uml.Package
    val umlClass = umlPackage1.packagedElements.first() as org.eclipse.uml2.uml.Class
    val att = umlClass.createOwnedAttribute("newAttribute", null)
    att.setIsStatic(true)
    umlView.commitChangesAndUpdate()
    umlView.close()

}