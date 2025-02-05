package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.UMLFactory
import org.eclipse.uml2.uml.VisibilityKind
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.uml.*

class ClassDiagramViewMapperTest {


    lateinit var eobjects : List<EObject>
    @BeforeEach
    fun setUp() {
        val factory = UMLFactory.eINSTANCE

        // Create a UML package to contain our model elements
        val examplePackage = factory.createPackage()
        examplePackage.name = "examplePackage"


        val booleanType = factory.createPrimitiveType().apply { name = "Boolean" }
        booleanType.name = "Boolean"
        // ---------------------------------------------------
        // 1. Create the PaymentProcessor interface
        // ---------------------------------------------------
        val paymentProcessor = examplePackage.createOwnedInterface("PaymentProcessor")

        // Add an operation "processPayment" to PaymentProcessor.
        val processPaymentOp = paymentProcessor.createOwnedOperation("processPayment", null, null)
        processPaymentOp.type = booleanType

        // Create a primitive type for Double.
        // (In a complete model, you might reuse an existing type from a standard library.)
        val doubleType = factory.createPrimitiveType().apply { name = "Double" }

        // Add a parameter "amount" of type Double to processPayment.
        val amountParam = processPaymentOp.createOwnedParameter("amount", doubleType)
        amountParam.direction = ParameterDirectionKind.IN_LITERAL

        // ---------------------------------------------------
        // 2. Create the CreditCardProcessor interface (a redefined interface)
        // ---------------------------------------------------
        val creditCardProcessor = examplePackage.createOwnedInterface("CreditCardProcessor")

        // Create a generalization so that CreditCardProcessor extends PaymentProcessor.
        // (This is UML’s way to show interface specialization.)
        creditCardProcessor.createGeneralization(paymentProcessor)

        // Add an operation "validateCard" to CreditCardProcessor.
        //create a type for the return value


        val validateCardOp = creditCardProcessor.createOwnedOperation("validateCard", null, null)
        validateCardOp.type = booleanType

        // Create a primitive type for String.
        val stringType = factory.createPrimitiveType().apply { name = "String" }

        // Add a parameter "cardNumber" of type String to validateCard.
        val cardNumberParam = validateCardOp.createOwnedParameter("cardNumber", stringType)
        cardNumberParam.direction = ParameterDirectionKind.IN_LITERAL

        // ---------------------------------------------------
        // 3. Create classes that implement these interfaces
        // ---------------------------------------------------
        // Create BasicPaymentProcessor that implements PaymentProcessor.
        val basicPaymentProcessor = examplePackage.createOwnedClass("BasicPaymentProcessor", false)
        basicPaymentProcessor.createInterfaceRealization("PaymentProcessorRealization", paymentProcessor)

        // Create AdvancedCreditCardProcessor that implements CreditCardProcessor.
        val advancedCreditCardProcessor = examplePackage.createOwnedClass("AdvancedCreditCardProcessor", false)
        advancedCreditCardProcessor.createInterfaceRealization("CreditCardProcessorRealization", creditCardProcessor)

        // ---------------------------------------------------
        // 4. Create a class that uses the PaymentProcessor interface
        // ---------------------------------------------------
        // PaymentService will depend on (use) PaymentProcessor.
        val paymentService = examplePackage.createOwnedClass("PaymentService", false)

        // Add an attribute "processor" of type PaymentProcessor.
        val processorAttr = paymentService.createOwnedAttribute("processor", paymentProcessor)
        processorAttr.visibility = VisibilityKind.PRIVATE_LITERAL

        // ---------------------------------------------------
        // 5. Package the model in a list for further processing or saving.
        // ---------------------------------------------------
         eobjects = listOf(examplePackage)
    }


    @Test
    fun testCreateContent() {
        val mapper = ClassDiagramViewMapper()
        val windows = mapper.mapViewToWindows(eobjects)
        windows.forEach(::println)

        val contents = mapper.mapEObjectsToWindowsContent(ClassDiagramContentSelector().applySelection(eobjects, windows))
        for (content in contents) {
            println(content.content.toString())
        }


    }

    @Test
    fun testEditContentButOnlyCreatingClasses() {
        val viewMapper = ClassDiagramViewMapper()
        val contentSelector = ClassDiagramContentSelector()
        val umlNodes = listOf(
            UmlNode("", "Class1", "",
            listOf(UmlAttribute(UmlVisibility.PUBLIC, "attribute1", "String")), listOf(), listOf()),
            UmlNode("", "Class2", "",
                listOf(UmlAttribute(UmlVisibility.PUBLIC, "attribute2", "int")), listOf(), listOf())
        )
        val umlConnections = listOf<UmlConnection>()
        val testUmlDiagram = UmlDiagram(umlNodes, umlConnections)
        val umlPackage = UMLFactory.eINSTANCE.createPackage()
        umlPackage.name = "examplePackage"
        val oldEObjects = listOf<EObject>(umlPackage)
        val contentSelection = contentSelector.applySelection(oldEObjects, viewMapper.mapViewToWindows(oldEObjects))
        viewMapper.mapWindowsToEObjectsAndApplyChangesToEObjects(
            contentSelection,
            listOf(Window("examplePackage", testUmlDiagram))
        )
        println(oldEObjects)



    }


}