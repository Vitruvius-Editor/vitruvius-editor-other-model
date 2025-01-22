package vitruv.tools.vitruvadpter.testServer

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.mwe2.launch.runtime.Mwe2Launcher
import tools.vitruv.change.interaction.UserInteractionFactory
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.framework.remote.server.VirtualModelInitializer
import tools.vitruv.framework.remote.server.VitruvServer
import tools.vitruv.framework.views.impl.IdentityMappingViewType
import tools.vitruv.framework.vsum.VirtualModelBuilder
import java.nio.file.Path


    fun main() {

        val vitruvServer = VitruvServer(init())
        vitruvServer.start()

        val vitruvClient = VitruvClientFactory.create("localhost", Path.of("vitruv_server/src/main/resources/temp"))
        val viewTypes = vitruvClient.viewTypes
        println(viewTypes)
        val selector = vitruvClient.createSelector(viewTypes.random())
        selector.selectableElements.forEach() {
            vitruvClient.createSelector(viewTypes.random()).setSelected(it, true)
        }
        val view = selector.createView()

        val testEClass = EcoreFactory.eINSTANCE.createEClass()
        testEClass.name = "EClass"
        testEClass.eStructuralFeatures.add(EcoreFactory.eINSTANCE.createEAttribute().apply {
            name = "test"
            eType = EcoreFactory.eINSTANCE.createEDataType().apply {
                instanceClassName = "int"
            }
        })

        val testEClass2 = EcoreFactory.eINSTANCE.createEClass()
        testEClass2.name = "EClass2"
        testEClass2.eStructuralFeatures.add(EcoreFactory.eINSTANCE.createEAttribute().apply {
            name = "test2"
            eType = EcoreFactory.eINSTANCE.createEDataType().apply {
                instanceClassName = "char"
            }
        })
        val eObjects = listOf(testEClass, testEClass2)
        val changeView = view.withChangeRecordingTrait()
        changeView.registerRoot(testEClass2, URI.createURI("vitruv_server/src/main/resources/models"))
        changeView.commitChanges()

    }

fun init() = VirtualModelInitializer {
    VirtualModelBuilder().withStorageFolder(Path.of("vitruv_server/src/main/resources/model")).withUserInteractor(
        UserInteractionFactory.instance.createUserInteractor(
            UserInteractionFactory.instance.createPredefinedInteractionResultProvider(null))).withViewType(IdentityMappingViewType("VT1")).buildAndInitialize()
}

fun createModel() {
    val workflowArgs = arrayOf("vitruv_server/generate.mwe2", "-p", "key=value")
    Mwe2Launcher().run(workflowArgs)
}


