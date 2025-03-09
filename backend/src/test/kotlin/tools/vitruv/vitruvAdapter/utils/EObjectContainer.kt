package tools.vitruv.vitruvAdapter.utils

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.PackageableElement
import org.eclipse.uml2.uml.UMLFactory
import org.mockito.Mockito
import tools.mdsd.jamopp.model.java.classifiers.Class
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier
import tools.mdsd.jamopp.model.java.classifiers.Interface
import tools.mdsd.jamopp.model.java.containers.CompilationUnit
import tools.mdsd.jamopp.model.java.containers.ContainersFactory
import tools.mdsd.jamopp.model.java.literals.LiteralsFactory
import tools.mdsd.jamopp.model.java.members.MembersFactory
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory
import tools.mdsd.jamopp.model.java.statements.StatementsFactory
import tools.mdsd.jamopp.model.java.types.TypesFactory
import java.math.BigInteger

/**
 * This utility class should contain some containers of EObjects, which can be used for testing.
 */
class EObjectContainer {

    val umlPackage = EResourceMock.mockERessourceForEObject(UMLFactory.eINSTANCE.createPackage())
    val javaPackage = EResourceMock.mockERessourceForEObject(ContainersFactory.eINSTANCE.createCompilationUnit())


    /**
     * Returns a container with a Java and Uml Class, only containing attributes.
     */
    fun getContainer1AsRootObjects(): List<EObject> {
        val javaPackage = getCompilationUnit1()
        val umlPackage = getUmlPackage1()
        addClassifiersToCompilationUnit(listOf(getSimplestJavaClass()), javaPackage)
        addClassifiersToUmlPackage(listOf(getSimpleUmlClass()), umlPackage)
        return listOf(javaPackage, umlPackage)
    }

    /**
     * Returns a container with a Java and Uml Class, containing attributes and methods.
     */
    fun getContainer2AsRootObjects(): List<EObject> {
        val javaPackage = getCompilationUnit1()
        val umlPackage = getUmlPackage1()
        addClassifiersToCompilationUnit(listOf(getSimplestJavaClass(), getJavaClassWithMethod()), javaPackage)
        addClassifiersToUmlPackage(listOf(getSimpleUmlClass(), getUmlClassWithMethod()), umlPackage)
        return listOf(javaPackage, umlPackage)
    }

    /**
     * Returns a container with a Java and Uml Class, containing attributes, methods and interfaces.
     */
    fun getContainer3AsRootObjects(): List<EObject> {
        val javaPackage = getCompilationUnit1()
        val umlPackage = getUmlPackage1()
        addClassifiersToCompilationUnit(
            listOf(
                getSimplestJavaClass(),
                getJavaClassWithMethod(),
                getJavaInterfaceWithMethod()
            ), javaPackage
        )
        addClassifiersToUmlPackage(
            listOf(getSimpleUmlClass(), getUmlClassWithMethod(), getUmlInterfaceWithMethod()),
            umlPackage
        )
        return listOf(javaPackage, umlPackage)
    }

    fun getContainerWith2Packages(): List<EObject> {
        val javaPackage1 = getCompilationUnit1()
        val javaPackage2 = getCompilationUnit2()
        val umlPackage1 = getUmlPackage1()
        val umlPackage2 = getUmlPackage2()
        addClassifiersToCompilationUnit(listOf(getSimplestJavaClass()), javaPackage1)
        addClassifiersToCompilationUnit(listOf(getJavaClassWithMethod()), javaPackage2)
        addClassifiersToUmlPackage(listOf(getSimpleUmlClass()), umlPackage1)
        addClassifiersToUmlPackage(listOf(getUmlClassWithMethod()), umlPackage2)
        return listOf(javaPackage1, javaPackage2, umlPackage1, umlPackage2)
    }

    fun getContainerWithClassExtends(): List<EObject> {
        val umlPackage = getUmlPackage1()
        val umlClass1 = getVerySimpleUmlClass()
        val umlClass2 = getSimpleUmlClass()
        val umlClass3 = getUmlClass3()
        val umlInterface = umlPackage.createOwnedInterface("Interface1")
        val umlInterfaceRealization = umlClass1.createInterfaceRealization("interfaceRealization1", umlInterface)
        val umlInterface2 = umlPackage.createOwnedInterface("Interface2")
        umlInterface.redefinedInterfaces.add(umlInterface2)
        umlClass1.interfaceRealizations.add(umlInterfaceRealization)
        umlClass1.superClasses.add(umlClass2)
        addClassifiersToUmlPackage(listOf(umlClass1, umlClass2, umlClass3, umlInterface, umlInterface2), umlPackage)
        return listOf(umlPackage)
    }

    /**
     * Returns a container with a Java and Uml Class, only containing attributes.
     */
    fun getContainerWithSimpleClass(): List<EObject> {
        return listOf(getSimplestJavaClass(), getSimpleUmlClass())
    }

    /**
     * Returns a container with a Java and Uml Class, containing attributes and methods.
     */
    fun getContainerWithClassesWithMethod(): List<EObject> {
        return listOf(getSimplestJavaClass(), getJavaClassWithMethod(), getSimpleUmlClass(), getUmlClassWithMethod())
    }

    /**
     * Returns a container with a Java and Uml Class, containing attributes, methods and interfaces.
     */
    fun getContainerWithClassesAndInterface(): List<EObject> {
        return listOf(
            getSimplestJavaClass(),
            getJavaClassWithMethod(),
            getJavaInterfaceWithMethod(),
            getSimpleUmlClass(),
            getUmlClassWithMethod(),
            getUmlInterfaceWithMethod()
        )
    }

    /**
     * Returns a container with an Uml Class and Interface
     */
    fun getUmlContainerWithInterfaceRealization(): List<EObject> {
        val umlClass = getVerySimpleUmlClass()
        val superClass = getSimpleUmlClass()
        umlClass.superClasses.add(superClass)
        val umlInterface = getSimpleUmlInterface()
        umlClass.createInterfaceRealization("realization", umlInterface)
        umlPackage.packagedElements.addAll(listOf(umlClass, umlInterface))
        return listOf(umlPackage)

    }


    private fun addClassifiersToUmlPackage(umlElements: List<PackageableElement>, umlPackage: Package) {
        umlPackage.packagedElements.addAll(umlElements)
    }

    private fun addClassifiersToCompilationUnit(javaElements: List<ConcreteClassifier>, javaPackage: CompilationUnit) {
        javaPackage.classifiers.addAll(javaElements)
    }

    private fun getCompilationUnit1(): CompilationUnit {
        val javaPackage = ContainersFactory.eINSTANCE.createCompilationUnit()
        javaPackage.name = "examplePackage"
        return EResourceMock.mockERessourceAndUuidForEObject(javaPackage)
    }

    private fun getCompilationUnit2(): CompilationUnit {
        val javaPackage = ContainersFactory.eINSTANCE.createCompilationUnit()
        javaPackage.name = "examplePackage2"
        return EResourceMock.mockERessourceAndUuidForEObject(javaPackage)
    }

    private fun getUmlPackage1(): Package {
        umlPackage.name = "examplePackage"
        return umlPackage
    }

    private fun getUmlPackage2(): Package {
        val umlPackage = UMLFactory.eINSTANCE.createPackage()
        umlPackage.name = "examplePackage2"
        return EResourceMock.mockERessourceAndUuidForEObject(umlPackage)
    }

    private fun getSimplestJavaClass(): Class {
        val javaClass = ClassifiersFactory.eINSTANCE.createClass()
        javaClass.name = "Class1"
        javaClass.makePublic()
        val booleanType = TypesFactory.eINSTANCE.createBoolean()

        val member1 = MembersFactory.eINSTANCE.createField()
        member1.name = "myBooleanAttribute"
        javaClass.members.add(member1)
        member1.typeReference = booleanType
        val initialValue1 = LiteralsFactory.eINSTANCE.createBooleanLiteral()
        initialValue1.isValue = true
        member1.initialValue = initialValue1

        return EResourceMock.mockERessourceAndUuidForEObject(javaClass)
    }

    private fun getJavaClassWithMethod(): Class {
        val javaClass = ClassifiersFactory.eINSTANCE.createClass()
        javaClass.name = "Class2"
        javaClass.makePublic()

        val member2 = MembersFactory.eINSTANCE.createField()
        member2.name = "myIntAttribute2"
        val intType2 = TypesFactory.eINSTANCE.createInt()
        member2.typeReference = intType2
        javaClass.members.add(EResourceMock.mockERessourceAndUuidForEObject(member2))

        val member3 = MembersFactory.eINSTANCE.createField()
        member3.name = "myIntAttribute3"
        val intType3 = TypesFactory.eINSTANCE.createInt()
        member3.typeReference = intType3
        javaClass.members.add(EResourceMock.mockERessourceAndUuidForEObject(member3))
        val method = MembersFactory.eINSTANCE.createClassMethod()
        method.name = "myMethod"
        method.makePublic()
        method.typeReference = TypesFactory.eINSTANCE.createInt()
        val parameter = ParametersFactory.eINSTANCE.createCatchParameter()
        parameter.name = "myParameter"
        parameter.typeReference = TypesFactory.eINSTANCE.createInt()
        method.parameters.add(EResourceMock.mockERessourceAndUuidForEObject(parameter))

        val block = StatementsFactory.eINSTANCE.createBlock()
        val statement = StatementsFactory.eINSTANCE.createReturn()
        val value = LiteralsFactory.eINSTANCE.createDecimalIntegerLiteral()
        value.decimalValue = BigInteger.valueOf(5)
        statement.returnValue = value
        method.statement = block
        method.block.statements.add(statement)
        javaClass.members.add(EResourceMock.mockERessourceAndUuidForEObject(method))

        return EResourceMock.mockERessourceAndUuidForEObject(javaClass)
    }


    private fun getJavaInterfaceWithMethod(): Interface {
        val javaInterface = ClassifiersFactory.eINSTANCE.createInterface()
        javaInterface.name = "Interface1"
        javaInterface.makePublic()

        val method = MembersFactory.eINSTANCE.createInterfaceMethod()
        method.name = "myInterfaceMethod"
        method.makePublic()
        method.typeReference = TypesFactory.eINSTANCE.createInt()
        val parameter = ParametersFactory.eINSTANCE.createCatchParameter()
        parameter.name = "myInterfaceParameter"
        parameter.typeReference = TypesFactory.eINSTANCE.createBoolean()
        method.parameters.add(EResourceMock.mockERessourceAndUuidForEObject(parameter))
        method.statement = StatementsFactory.eINSTANCE.createBlock()
        javaInterface.members.add(EResourceMock.mockERessourceAndUuidForEObject(method))

        return EResourceMock.mockERessourceAndUuidForEObject(javaInterface)
    }


    private fun getSimpleUmlInterface(): org.eclipse.uml2.uml.Interface {
        val umlInterface = UMLFactory.eINSTANCE.createInterface()
        umlInterface.name = "Interface1"
        val method = UMLFactory.eINSTANCE.createOperation()
        method.name = "myMethod"
        method.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL
        method.type = UMLFactory.eINSTANCE.createPrimitiveType()
        method.type.name = "int"
        val parameter = UMLFactory.eINSTANCE.createParameter()
        parameter.name = "myParameter"
        parameter.type = UMLFactory.eINSTANCE.createPrimitiveType()
        parameter.type.name = "int"
        method.ownedParameters.add(EResourceMock.mockERessourceAndUuidForEObject(parameter))
        umlInterface.ownedOperations.add(EResourceMock.mockERessourceAndUuidForEObject(method))
        return EResourceMock.mockERessourceAndUuidForEObject(umlInterface, umlPackage)
    }

    private fun getUmlInterfaceWithMethod(): org.eclipse.uml2.uml.Interface {
        val umlInterface = UMLFactory.eINSTANCE.createInterface()
        umlInterface.name = "Interface1"
        val method = UMLFactory.eINSTANCE.createOperation()
        method.name = "myMethod"
        method.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL
        method.type = UMLFactory.eINSTANCE.createPrimitiveType()
        method.type.name = "int"
        val parameter = UMLFactory.eINSTANCE.createParameter()
        parameter.name = "myParameter"
        parameter.type = UMLFactory.eINSTANCE.createPrimitiveType()
        parameter.type.name = "int"
        method.ownedParameters.add(EResourceMock.mockERessourceAndUuidForEObject(parameter))
        umlInterface.ownedOperations.add(EResourceMock.mockERessourceAndUuidForEObject(method))
        return EResourceMock.mockERessourceAndUuidForEObject(umlInterface, umlPackage)
    }

    private fun getSimpleUmlClass(): org.eclipse.uml2.uml.Class {
        val umlClass = UMLFactory.eINSTANCE.createClass()
        umlClass.name = "Class1"
        val attribute = UMLFactory.eINSTANCE.createProperty()
        attribute.name = "myIntAttribute"
        attribute.type = EResourceMock.mockERessourceAndUuidForEObject(UMLFactory.eINSTANCE.createPrimitiveType())
        attribute.type.name = "int"

        val attribute2 = UMLFactory.eINSTANCE.createProperty()
        attribute2.name = "myBooleanAttribute"
        attribute2.type = EResourceMock.mockERessourceAndUuidForEObject(UMLFactory.eINSTANCE.createPrimitiveType())
        attribute2.type.name = "boolean"
        umlClass.ownedAttributes.add(EResourceMock.mockERessourceAndUuidForEObject(attribute))
        umlClass.ownedAttributes.add(EResourceMock.mockERessourceAndUuidForEObject(attribute2))
        return EResourceMock.mockERessourceAndUuidForEObject(umlClass, umlPackage)
    }

    private fun getVerySimpleUmlClass(): org.eclipse.uml2.uml.Class {
        val umlClass = UMLFactory.eINSTANCE.createClass()
        umlClass.name = "Class1"
        val attribute = UMLFactory.eINSTANCE.createProperty()
        attribute.name = "myIntAttribute"
        attribute.type = EResourceMock.mockERessourceAndUuidForEObject(UMLFactory.eINSTANCE.createPrimitiveType(), umlPackage)
        attribute.type.name = "int"
        umlClass.ownedAttributes.add(EResourceMock.mockERessourceAndUuidForEObject(attribute, umlPackage))
        val method = UMLFactory.eINSTANCE.createOperation()
        method.name = "myMethod"
        method.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL
        method.type = UMLFactory.eINSTANCE.createPrimitiveType()
        method.type.name = "int"
        val parameter = UMLFactory.eINSTANCE.createParameter()
        parameter.name = "myParameter"
        parameter.type = UMLFactory.eINSTANCE.createPrimitiveType()
        parameter.type.name = "int"
        method.ownedParameters.add(EResourceMock.mockERessourceAndUuidForEObject(parameter, umlPackage))
        umlClass.ownedOperations.add(EResourceMock.mockERessourceAndUuidForEObject(method, umlPackage))
        return EResourceMock.mockERessourceAndUuidForEObject(umlClass, umlPackage)
    }


    private fun getUmlClassWithMethod(): org.eclipse.uml2.uml.Class {
        val umlClass = UMLFactory.eINSTANCE.createClass()
        umlClass.name = "Class2"
        val attribute = UMLFactory.eINSTANCE.createProperty()
        attribute.name = "myIntAttribute2"
        attribute.type = UMLFactory.eINSTANCE.createPrimitiveType()
        attribute.type.name = "int"

        val attribute2 = UMLFactory.eINSTANCE.createProperty()
        attribute2.name = "myIntAttribute3"
        attribute2.type = EResourceMock.mockERessourceAndUuidForEObject(UMLFactory.eINSTANCE.createPrimitiveType())
        attribute2.type.name = "int"

        val method = UMLFactory.eINSTANCE.createOperation()
        method.name = "myMethod"
        method.visibility = org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL
        method.type = UMLFactory.eINSTANCE.createPrimitiveType()
        method.type.name = "int"
        val parameter = UMLFactory.eINSTANCE.createParameter()
        parameter.name = "myParameter"
        parameter.type = UMLFactory.eINSTANCE.createPrimitiveType()
        parameter.type.name = "int"
        method.type = UMLFactory.eINSTANCE.createPrimitiveType()
        method.type.name = "int"
        method.ownedParameters.add(EResourceMock.mockERessourceAndUuidForEObject(parameter))
        umlClass.ownedOperations.add(EResourceMock.mockERessourceAndUuidForEObject(method))
        umlClass.ownedAttributes.add(EResourceMock.mockERessourceAndUuidForEObject(attribute))
        umlClass.ownedAttributes.add(EResourceMock.mockERessourceAndUuidForEObject(attribute2))
        return EResourceMock.mockERessourceAndUuidForEObject(umlClass, umlPackage)
    }

    private fun getUmlClass3(): org.eclipse.uml2.uml.Class {
        val umlClass = UMLFactory.eINSTANCE.createClass()
        umlClass.name = "Class3"

        val privateAttribute = UMLFactory.eINSTANCE.createProperty()
        privateAttribute.name = "myPrivateAttribute"
        privateAttribute.visibility = org.eclipse.uml2.uml.VisibilityKind.PRIVATE_LITERAL

        val protectedAttribute = UMLFactory.eINSTANCE.createProperty()
        protectedAttribute.name = "myProtectedAttribute"
        protectedAttribute.visibility = org.eclipse.uml2.uml.VisibilityKind.PROTECTED_LITERAL

        val packageAttribute = UMLFactory.eINSTANCE.createProperty()
        packageAttribute.name = "myPackageAttribute"
        packageAttribute.visibility = org.eclipse.uml2.uml.VisibilityKind.PACKAGE_LITERAL

        umlClass.ownedAttributes.addAll(listOf(privateAttribute, protectedAttribute, packageAttribute))

        return EResourceMock.mockERessourceForEObject(umlClass)
    }

}