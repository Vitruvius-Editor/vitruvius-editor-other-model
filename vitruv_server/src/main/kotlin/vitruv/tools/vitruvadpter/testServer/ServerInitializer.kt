package vitruv.tools.vitruvadpter.testServer


import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.plugin.EcorePlugin
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.uml2.uml.Type
import org.eclipse.uml2.uml.UMLFactory
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.VisibilityKind
import org.eclipse.uml2.uml.internal.impl.LiteralIntegerImpl
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl
import tools.mdsd.jamopp.model.java.JavaPackage
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.model.java.containers.ContainersFactory
import tools.mdsd.jamopp.model.java.members.MembersFactory
import tools.mdsd.jamopp.model.java.types.TypesFactory
import tools.mdsd.jamopp.parser.jdt.singlefile.JaMoPPJDTSingleFileParser
import tools.vitruv.applications.util.temporary.java.JavaSetup
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
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

/**
 * Initializes the server
 */

class ServerInitializer {

    val serverPort: Int = 8000
    val host: String = "localhost"
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
        generatePackage()
        generateJavaCode()
        return server
    }


    private fun generateJavaCode() {

        val fileName = "TestClass.java"
        try {
            val inputStream: InputStream = FileInputStream("vitruv_server/src/main/resources/Dodo.java")
            val rootw = JaMoPPJDTSingleFileParser().parse(fileName, inputStream) as CompilationUnit
            val classs = rootw.classifiers[0] as tools.mdsd.jamopp.model.java.classifiers.Class


//
//            val classJava = ClassifiersFactory.eINSTANCE.createClass()
//            val obejectClass = classJava.objectClass
//
//            for (classifier in root.classifiers){
//                if(classifier is Class){
//                    classifier.defaultExtends = null
//                }
//            }
            val umlPackage = UMLFactory.eINSTANCE.createPackage()
            umlPackage.name = "examplePackage2"

            val root = ClassifiersFactory.eINSTANCE.createClass()
            root.name = "Dodo"
            root.makePublic()
            val member = MembersFactory.eINSTANCE.createField()
            member.name = "myIntAttribute"
            root.members.add(member)

            val intType = TypesFactory.eINSTANCE.createInt()
            member.typeReference = intType


            val newClass = ClassifiersFactory.eINSTANCE.createClass()
            newClass.name = "Dodo1"
            root.makePublic()
            val member1 = MembersFactory.eINSTANCE.createField()
            member1.name = "myIntAttribute1"
            root.members.add(member1)
            val intType1 = tools.mdsd.jamopp.model.java.types.TypesFactory.eINSTANCE.createInt()
            member1.typeReference = intType


            val javaPackage = ContainersFactory.eINSTANCE.createPackage()
            javaPackage.name = "testPackage"
//            javaPackage.classifiers.add(root)
//            javaPackage.classifiers.add(newClass)
            //javaPackage.classifiers.add(classs)


            val view = getJavaView().withChangeDerivingTrait()
            view.registerRoot(umlPackage, umlUri)
            view.commitChanges()
            view.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generatePackage() {
        val factory = UMLFactory.eINSTANCE
        val examplePackage = factory.createPackage()
        examplePackage.name = "examplePackage"

        val umlInterface = examplePackage.createOwnedInterface("Interface1")


        val umlClass = examplePackage.createOwnedClass("Class1", false)

        umlClass.setIsFinalSpecialization(true)


        val class2 = examplePackage.createOwnedClass("Class2", true)
        val intatt = class2.createOwnedAttribute("myIntAttribute", null)
        class2.createOwnedOperation("myOperation", null, null)

        val intType = factory.createPrimitiveType()
        intType.name = "int"

        val initialValue2 = factory.createLiteralInteger()
        (initialValue2 as LiteralIntegerImpl).value = 5
        intatt.defaultValue = initialValue2


        val class1att = umlClass.createOwnedAttribute("myIntAttribute", intType)

        val initialValue1 = factory.createLiteralInteger()
        (initialValue2 as LiteralIntegerImpl).value = 20
        class1att.defaultValue = initialValue2

        examplePackage.packagedElements.add(intType)

        umlClass.superClasses.add(class2)
        umlClass.createInterfaceRealization("interfaceRealization", umlInterface)


        val attribute = umlClass.createOwnedAttribute("myIntAttribute", null)
        attribute.visibility = VisibilityKind.PUBLIC_LITERAL

        val operationParameterNames: EList<String> = BasicEList<String>()
        operationParameterNames.add("param1")
        operationParameterNames.add("param2")


        val operationParameterTypes: EList<Type> = BasicEList<Type>()
        operationParameterTypes.add(intType)
        operationParameterTypes.add(intType)

        val operation = umlClass.createOwnedOperation("myOperation", operationParameterNames, operationParameterTypes)
        operation.type = intType

        //add body to operation
        val body = factory.createOpaqueBehavior()
        body.name = "getWindowsBody"
        body.languages.add("Kotlin")
        body.bodies.add(
            """
            System.out.println("Hello World");
        """.trimIndent()
        )

        var view = getUMLView().withChangeDerivingTrait()
        view.registerRoot(examplePackage, umlUri)
        view.commitChanges()
        view.close()

    }


    private fun registerFactories() {
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("*", XMIResourceFactoryImpl())
        Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("uml", UMLResourceFactoryImpl())
        EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE)
        EPackage.Registry.INSTANCE.put(JavaPackage.eNS_URI, JavaPackage.eINSTANCE)
        EPackage.Registry.INSTANCE.put(CorrespondencePackage.eNS_URI, CorrespondencePackageImpl.eINSTANCE)
        EPackage.Registry.INSTANCE.put(AtomicPackage.eNS_URI, AtomicPackageImpl.eINSTANCE)

        JavaSetup.prepareFactories()
        JavaSetup.resetClasspathAndRegisterStandardLibrary()

        EcorePlugin.ExtensionProcessor.process(null)
    }

    private fun getJavaView(): View {
        return getView("JAVA", javaUri)
    }

    private fun getUMLView(): View {
        return getView("UML", umlUri)
    }

    private fun getView(viewTypeName: String, srcUri: URI): View {
        var selector = vsum.createSelector(viewTypes[viewTypeName])
        selector.selectableElements.stream().filter { it.eResource().uri == srcUri }
            .forEach { element -> selector.setSelected(element, true) }
        return selector.createView()
    }


    private fun createViewTypes(): Map<String, ViewType<*>> {
        val viewTypes = HashMap<String, ViewType<*>>()
        viewTypes.put("JAVA", IdentityMappingViewType("JAVA"))
        viewTypes.put("UML", IdentityMappingViewType("UML"))
        return viewTypes
    }

    private fun init(rootPath: Path): VirtualModel {
        return VirtualModelBuilder()
            //change propagation specification commented out because it's not working on client side
//            .withChangePropagationSpecification(UmlToJavaChangePropagationSpecification())
//            .withChangePropagationSpecification(JavaToUmlChangePropagationSpecification())
            .withStorageFolder(rootPath)
            .withViewTypes(viewTypes.values)
            .withUserInteractorForResultProvider(TestInteractionResultProvider())
            .buildAndInitialize()
    }

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
