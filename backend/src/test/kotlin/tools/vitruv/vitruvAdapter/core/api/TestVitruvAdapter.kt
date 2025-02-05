package tools.vitruv.vitruvAdapter.core.api

import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.plugin.EcorePlugin
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.internal.impl.UMLPackageImpl
import org.eclipse.uml2.uml.internal.resource.UML22UMLResourceFactoryImpl
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import tools.mdsd.jamopp.model.java.JavaPackage
import tools.mdsd.jamopp.model.java.impl.JavaPackageImpl
import tools.vitruv.applications.util.temporary.java.JamoppLibraryHelper
import tools.vitruv.applications.util.temporary.java.JavaSetup
import tools.vitruv.change.atomic.AtomicPackage
import tools.vitruv.change.atomic.impl.AtomicPackageImpl
import tools.vitruv.change.correspondence.CorrespondencePackage
import tools.vitruv.change.correspondence.impl.CorrespondencePackageImpl
import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.vitruvAdapter.core.impl.DefaultDisplayViewRepositoryFactory
import tools.vitruv.vitruvAdapter.core.impl.DisplayViewRepository
import tools.vitruv.vitruvAdapter.core.impl.GenericDisplayView
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableContentSelector
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableViewMapper
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeViewMapper
import tools.vitruv.vitruvAdapter.core.impl.selector.AllSelector
import tools.vitruv.vitruvAdapter.core.impl.sourceCodeView.SourceCodeContentSelector
import java.nio.file.Path


class TestVitruvAdapter {
    lateinit var adapter: VitruvAdapter
    lateinit var displayViewRepository: DisplayViewRepository

    @BeforeEach
    fun initVitruvAdapter() {
        EcorePlugin.ExtensionProcessor.process(null)

        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("*", XMIResourceFactoryImpl())
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("uml", UMLResourceFactoryImpl())
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("uml2", UML22UMLResourceFactoryImpl())


        EPackage.Registry.INSTANCE.put(JavaPackage.eNS_URI, JavaPackage.eINSTANCE)
        EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE)
        EPackage.Registry.INSTANCE.put(JavaPackage.eNS_URI, JavaPackageImpl.eINSTANCE)
        EPackage.Registry.INSTANCE.put(CorrespondencePackage.eNS_URI, CorrespondencePackageImpl.eINSTANCE)
        EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackageImpl.eINSTANCE)
        EPackage.Registry.INSTANCE.put(AtomicPackage.eNS_URI, AtomicPackageImpl.eINSTANCE)

        JamoppLibraryHelper.registerStdLib()
        JavaSetup.prepareFactories()
        JavaSetup.resetClasspathAndRegisterStandardLibrary()

        val vitruvClient: VitruvClient = VitruvClientFactory.create("localhost", 8000, Path.of("tools/vitruv/vitruvAdapter/tmp"))
//        val vitruvClient: VitruvClient = mock(VitruvClient::class.java)

        adapter = VitruvAdapter()


        val contentSelector = SourceCodeContentSelector()
        displayViewRepository = DefaultDisplayViewRepositoryFactory().createDisplayViewRepository()
        val sourceCodeDisplayView = GenericDisplayView("SourceCode", "UML", SourceCodeViewMapper() as ViewMapper<Any?>, AllSelector(),
            contentSelector as ContentSelector<Any?>)
        val classTableContentSelector = ClassTableContentSelector()
        val classTableDisplayView = GenericDisplayView("ClassTable", "UML", ClassTableViewMapper() as ViewMapper<Any?>, AllSelector(),
            classTableContentSelector as ContentSelector<Any?>)

        adapter.connectClient(vitruvClient)
        displayViewRepository.registerDisplayView(sourceCodeDisplayView)
        displayViewRepository.registerDisplayView(classTableDisplayView)
        adapter.setDisplayViewContainer(displayViewRepository)
    }

    @Test
    fun testGetDisplayViews() {
        val displayViews = adapter.getDisplayViews()
        print(displayViews)
    }

    @Test
    fun testGetWindows(){
        val displayView = displayViewRepository.getDisplayView("SourceCode")!!
        val windows = adapter.getWindows(displayView)
        println(windows)
        val content = adapter.createWindowContent(displayView, windows)
        print(content)
    }
}