package vitruv.tools.vitruvadpter.testServer

import edu.kit.ipd.sdq.metamodels.families.FamiliesPackage
import edu.kit.ipd.sdq.metamodels.families.impl.FamiliesFactoryImpl
import edu.kit.ipd.sdq.metamodels.families.impl.FamiliesPackageImpl
import edu.kit.ipd.sdq.metamodels.persons.PersonsPackage
import edu.kit.ipd.sdq.metamodels.persons.impl.PersonsFactoryImpl
import edu.kit.ipd.sdq.metamodels.persons.impl.PersonsPackageImpl
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.plugin.EcorePlugin
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import tools.vitruv.change.atomic.AtomicPackage
import tools.vitruv.change.atomic.impl.AtomicPackageImpl
import tools.vitruv.change.correspondence.CorrespondencePackage
import tools.vitruv.change.correspondence.impl.CorrespondencePackageImpl
import tools.vitruv.change.utils.ProjectMarker
import tools.vitruv.dsls.demo.familiespersons.families2persons.FamiliesToPersonsChangePropagationSpecification
import tools.vitruv.dsls.demo.familiespersons.persons2families.PersonsToFamiliesChangePropagationSpecification
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
    lateinit var personsPath: Path
    lateinit var familiesPath: Path
    lateinit var personsUri: URI
    lateinit var familiesUri: URI
    lateinit var server: VitruvServer

    /**
     * Initializes the server
     */
    fun initialize(rootPath: Path): VitruvServer {
        personsPath = rootPath.resolve("model/persons")
        familiesPath = rootPath.resolve("model/families")
        personsUri = URI.createFileURI(personsPath.toString())
        familiesUri = URI.createFileURI(familiesPath.toString())
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
        return server
    }


    private fun registerFactories() {
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap
            .put("*", XMIResourceFactoryImpl())
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap
            .put("persons", PersonsFactoryImpl())
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap
            .put("families", FamiliesFactoryImpl())
        EPackage.Registry.INSTANCE.put(CorrespondencePackage.eNS_URI, CorrespondencePackageImpl.eINSTANCE)
        EPackage.Registry.INSTANCE.put(AtomicPackage.eNS_URI, AtomicPackageImpl.eINSTANCE)
        EPackage.Registry.INSTANCE.put(FamiliesPackage.eNS_URI, FamiliesPackageImpl.eINSTANCE)
        EPackage.Registry.INSTANCE.put(PersonsPackage.eNS_URI, PersonsPackageImpl.eINSTANCE)

        EcorePlugin.ExtensionProcessor.process(null)
    }


    fun getFamilyView(): View {
        return getView("Family", familiesUri)
    }

    fun getPersonView(): View {
        return getView("Person", personsUri)
    }

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
        viewTypes.put("Person", IdentityMappingViewType("Person"))
        viewTypes.put("Family", IdentityMappingViewType("Family"))
        return viewTypes
    }

    private fun init(rootPath: Path): VirtualModel =
        VirtualModelBuilder()
            // change propagation specification commented out because it's not working on client side
            .withChangePropagationSpecification(FamiliesToPersonsChangePropagationSpecification())
            .withChangePropagationSpecification(PersonsToFamiliesChangePropagationSpecification())
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
