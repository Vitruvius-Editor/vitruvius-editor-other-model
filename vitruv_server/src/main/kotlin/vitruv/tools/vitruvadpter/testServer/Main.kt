package vitruv.tools.vitruvadpter.testServer

import org.eclipse.uml2.uml.UMLFactory

import tools.vitruv.framework.remote.client.VitruvClientFactory

fun main() {
    val serverInitializer = ServerInitializer()
    val server = serverInitializer.initialize()
    var factory = UMLFactory.eINSTANCE
    var newClass = factory.createClass()
    newClass.name = "NewClass"
//    rootPackage.packagedElements.add(newClass)
//    umlView.commitChanges()
//    umlView.close()


    server.start()
    println("Vitruvius server started on: " + serverInitializer.serverPort);

    //start client
    val client = VitruvClientFactory.create("localhost", serverInitializer.serverPort, serverInitializer.rootPath)
    var clientViewTypes = client.viewTypes

    var umlViewType = clientViewTypes.stream().filter{it.name == "UML"}.findAny()

    print(umlViewType.get().name)

    var umlSelector = umlViewType.get().createSelector(null)
    umlSelector.selectableElements.forEach { it -> umlSelector.setSelected(it, true) }

    var umlView = umlSelector.createView().withChangeDerivingTrait()
    val umlPackage = umlView.rootObjects.first() as org.eclipse.uml2.uml.Package
    umlPackage.packagedElements.add(newClass)
    umlView.commitChangesAndUpdate()

    print(umlPackage.packagedElements)
    umlView.close()









}