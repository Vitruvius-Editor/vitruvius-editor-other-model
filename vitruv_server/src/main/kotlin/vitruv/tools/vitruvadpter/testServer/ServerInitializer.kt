package vitruv.tools.vitruvadpter.testServer

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import tools.vitruv.change.interaction.UserInteractionFactory
import tools.vitruv.framework.remote.server.VirtualModelInitializer
import tools.vitruv.framework.remote.server.VitruvServer
import tools.vitruv.framework.views.impl.IdentityMappingViewType
import tools.vitruv.framework.vsum.VirtualModelBuilder
import java.nio.file.Path
import org.eclipse.uml2.uml.*
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl
import tools.vitruv.framework.views.ViewType
import tools.vitruv.framework.views.View
import tools.vitruv.framework.vsum.VirtualModel
import java.io.IOException
import java.nio.file.Files


class ServerInitializer {
    val  rootPath: Path = Path.of("vitruv_server/src/main/resources/model")

    val viewTypes: Map<String, ViewType<*>> = createViewTypes()
    val javaPath: Path = Path.of("vitruv_server/src/main/resources/model/java")
    val umlPath: Path = Path.of("vitruv_server/src/main/resources/model/uml")
    val javaUri: URI = URI.createFileURI(javaPath.toString())
    val umlUri: URI = URI.createFileURI(umlPath.toString())

    val vsum: VirtualModel = init()
    val serverPort: Int = 8000


    fun initialize(): VitruvServer {
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("*", UMLResourceFactoryImpl())
        val vitruvServer = VitruvServer(VirtualModelInitializer { vsum }, serverPort, "localhost")
        generatePackage()
        return vitruvServer
    }

    private fun generatePackage() {
        val factory = UMLFactory.eINSTANCE
        val examplePackage = factory.createPackage()
        examplePackage.name = "examplePackage"
        var view = getUMLView().withChangeDerivingTrait()
        view.registerRoot(examplePackage, umlUri)
        view.commitChanges()
        view.close()
    }

    fun getJavaView(): View {
        return getView("JAVA", javaUri)
    }

    fun getUMLView(): View {
        return getView("UML", umlUri)
    }


    private fun getView(viewTypeName: String, srcUri: URI): View {
        var selector = vsum.createSelector(viewTypes[viewTypeName]);
        selector.selectableElements.stream().filter { it.eResource().uri == srcUri }
            .forEach { element -> selector.setSelected(element, true) }
        return selector.createView();
    }


    private fun createViewTypes(): Map<String, ViewType<*>> {
        val viewTypes = HashMap<String, ViewType<*>>()
        viewTypes.put("JAVA", IdentityMappingViewType("JAVA"))
        viewTypes.put("UML", IdentityMappingViewType("UML"))
        return viewTypes
    }

    private fun init() : VirtualModel {


        if (Files.exists(rootPath)) {
            deletePath(rootPath);
        } else {
            Files.createDirectories(rootPath);
        }

        return VirtualModelBuilder()
            .withStorageFolder(rootPath)
            .withUserInteractor(
                UserInteractionFactory.instance.createUserInteractor(
                    UserInteractionFactory.instance.createPredefinedInteractionResultProvider(
                        null
                    )
                )
            )
            .withViewTypes(viewTypes.values)
            .buildAndInitialize()
    }
    fun deletePath(pathToDelete: Path) {
        if (Files.isRegularFile(pathToDelete)) {
            try {
                Files.deleteIfExists(pathToDelete);
            } catch (ioEx : IOException) {
                throw IllegalStateException("The path " + pathToDelete.toString() + " should be removable.", ioEx);
            }
        } else if (Files.isDirectory(pathToDelete)) {
            try {
                Files.list(pathToDelete).forEach(this::deletePath);
            } catch (ioEx : IOException) {
                throw IllegalStateException("Could not iterate over the path " + pathToDelete.toString(), ioEx);
            }
        }
    }
}

