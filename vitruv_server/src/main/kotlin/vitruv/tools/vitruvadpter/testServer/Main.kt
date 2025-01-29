package vitruv.tools.vitruvadpter.testServer

import org.eclipse.uml2.uml.UMLFactory
import tools.mdsd.jamopp.model.java.containers.JavaRoot
import tools.vitruv.framework.remote.client.VitruvClientFactory
import java.nio.file.Path
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.mdsd.jamopp.printer.JaMoPPPrinter
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files

/**
 * Starts the server
 */

fun main() {
//    val serverInitializer = ServerInitializer()
//    val server = serverInitializer.initialize()
//    server.start()
//    println("Vitruvius server started on: " + serverInitializer.serverPort)


    val javaFile = File("C:\\Users\\amira\\Desktop\\vitruvius-editor2\\vitruv_server\\src\\main\\kotlin\\vitruv\\tools\\vitruvadpter\\testServer\\TestClass.java")

    val fileName = "TestClass.java"
    try{
        val inputStream: InputStream = FileInputStream("C:\\Users\\amira\\Desktop\\vitruvius-editor2\\vitruv_server\\src\\main\\kotlin\\vitruv\\tools\\vitruvadpter\\testServer\\TestClass.java")
        val root = JaMoPPJDTSingleFileParser().parse(fileName, inputStream)
        JaMoPPPrinter.print(root ,Path.of("src/main/resources/output.txt"))
    } catch (e: Exception){
        e.printStackTrace()
    }



    //clientTest(serverInitializer)
}


/**
 * Test the client
 */
fun clientTest(serverInitializer: ServerInitializer) {
    val client = VitruvClientFactory.create("localhost", serverInitializer.serverPort, Path.of("vitruv_server/test"))
    var clientViewTypes = client.viewTypes

    var umlViewType = clientViewTypes.stream().filter { it.name == "UML" }.findAny()

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