package vitruv.tools.vitruvadpter.testServer

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.plugin.EcorePlugin
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl
import tools.mdsd.jamopp.model.java.JavaPackage
import tools.vitruv.applications.util.temporary.java.*
import tools.vitruv.change.atomic.AtomicPackage
import tools.vitruv.change.atomic.impl.AtomicPackageImpl
import tools.vitruv.change.correspondence.CorrespondencePackage
import tools.vitruv.change.correspondence.impl.CorrespondencePackageImpl
import tools.vitruv.change.utils.ProjectMarker
import tools.vitruv.framework.remote.server.VirtualModelInitializer
import tools.vitruv.framework.remote.server.VitruvServer
import tools.vitruv.framework.views.View
import tools.vitruv.framework.views.ViewType
import tools.vitruv.framework.views.impl.IdentityMappingViewType
import tools.vitruv.framework.vsum.VirtualModel
import tools.vitruv.framework.vsum.VirtualModelBuilder
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

/**
 * Initializes the server
 */

class ServerInitializer(
    val host: String,
    val serverPort: Int,
) {
    lateinit var viewTypes: Map<String, ViewType<*>>
    lateinit var vsum: VirtualModel
    lateinit var javaPath: Path
    lateinit var umlPath: Path
    lateinit var javaUri: URI
    lateinit var umlUri: URI
    lateinit var server: VitruvServer

    /**
     * Initializes the server
     */
    fun initialize(rootPath: Path): VitruvServer {
        javaPath = rootPath.resolve("model/java")
        umlPath = rootPath.resolve("model/uml")
        javaUri = URI.createFileURI(javaPath.toString())
        umlUri = URI.createFileURI(umlPath.toString())
        registerFactories()

        if (Files.exists(rootPath)) {
            deletePath(rootPath)
        } else {
            Files.createDirectories(rootPath)
        }

        ProjectMarker.markAsProjectRootFolder(rootPath)

        viewTypes = createViewTypes()
        vsum = init(rootPath)

        server = VitruvServer(VirtualModelInitializer { vsum }, serverPort, host)
        registerUMLExampleModel()
        registerJavaExampleModel()
        return server
    }

    private fun registerJavaExampleModel() {
        val view = getJavaView().withChangeDerivingTrait()
        view.registerRoot(DemoModel.createJavaModel(), javaUri)
        view.commitChanges()
        view.close()
    }

    private fun registerUMLExampleModel() {
        val view = getUMLView().withChangeDerivingTrait()
        view.registerRoot(DemoModel.createUmlModel(), umlUri)
        view.commitChanges()
        view.close()
    }

    private fun registerFactories() {
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap
            .put("*", XMIResourceFactoryImpl())
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap
            .put("uml", UMLResourceFactoryImpl())
        EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE)
        EPackage.Registry.INSTANCE.put(JavaPackage.eNS_URI, JavaPackage.eINSTANCE)
        EPackage.Registry.INSTANCE.put(CorrespondencePackage.eNS_URI, CorrespondencePackageImpl.eINSTANCE)
        EPackage.Registry.INSTANCE.put(AtomicPackage.eNS_URI, AtomicPackageImpl.eINSTANCE)

        JavaSetup.prepareFactories()
        JavaSetup.resetClasspathAndRegisterStandardLibrary()

        EcorePlugin.ExtensionProcessor.process(null)
    }

    private fun getJavaView(): View = getView("JAVA", javaUri)

    private fun getUMLView(): View = getView("UML", umlUri)

    private fun getView(
        viewTypeName: String,
        srcUri: URI,
    ): View {
        var selector = vsum.createSelector(viewTypes[viewTypeName])
        selector.selectableElements
            .stream()
            .filter { it.eResource().uri == srcUri }
            .forEach { element -> selector.setSelected(element, true) }
        return selector.createView()
    }

    private fun createViewTypes(): Map<String, ViewType<*>> {
        val viewTypes = HashMap<String, ViewType<*>>()
        viewTypes.put("JAVA", IdentityMappingViewType("JAVA"))
        viewTypes.put("UML", IdentityMappingViewType("UML"))
        return viewTypes
    }

    private fun init(rootPath: Path): VirtualModel =
        VirtualModelBuilder()
            // change propagation specification commented out because it's not working on client side
//            .withChangePropagationSpecification(UmlToJavaChangePropagationSpecification())
//            .withChangePropagationSpecification(JavaToUmlChangePropagationSpecification())
            .withStorageFolder(rootPath)
            .withViewTypes(viewTypes.values)
            .withUserInteractorForResultProvider(TestInteractionResultProvider())
            .buildAndInitialize()

    private fun deletePath(pathToDelete: Path) {
        if (Files.isRegularFile(pathToDelete)) {
            try {
                Files.deleteIfExists(pathToDelete)
            } catch (ioEx: IOException) {
                throw IllegalStateException("The path $pathToDelete should be removable.", ioEx)
            }
        } else if (Files.isDirectory(pathToDelete)) {
            try {
                Files.list(pathToDelete).forEach(this::deletePath)
            } catch (ioEx: IOException) {
                throw IllegalStateException("Could not iterate over the path $pathToDelete", ioEx)
            }
        }
    }
}
