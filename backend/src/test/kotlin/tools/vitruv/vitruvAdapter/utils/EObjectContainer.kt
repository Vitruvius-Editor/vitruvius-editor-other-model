package tools.vitruv.vitruvAdapter.utils

import org.eclipse.emf.ecore.EObject
import org.eclipse.emfcloud.jackson.utils.EObjects
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.PackageableElement
import org.eclipse.uml2.uml.UMLFactory
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
class EObjectContainer private constructor() {

    companion object {

        /**
         * Returns a container with a Java and Uml Class, only containing attributes.
         */
        fun getContainer1AsRootObjects(): List<EObject> {
            val javaPackage = getCompilationUnit1()
            val umlPackage = getUmlPackage1()
            addClassifiersToCompilationUnit(listOf(getJavaClass1()), javaPackage)
            addClassifiersToUmlPackage(listOf(getUmlClass1()), umlPackage)
            return listOf(javaPackage, umlPackage)
        }

        /**
         * Returns a container with a Java and Uml Class, containing attributes and methods.
         */
        fun getContainer2AsRootObjects(): List<EObject> {
            val javaPackage = getCompilationUnit1()
            val umlPackage = getUmlPackage1()
            addClassifiersToCompilationUnit(listOf(getJavaClass1(), getJavaClass2()), javaPackage)
            addClassifiersToUmlPackage(listOf(getUmlClass1(), getUmlClass2()), umlPackage)
            return listOf(javaPackage, umlPackage)
        }

        /**
         * Returns a container with a Java and Uml Class, containing attributes, methods and interfaces.
         */
        fun getContainer3AsRootObjects(): List<EObject> {
            val javaPackage = getCompilationUnit1()
            val umlPackage = getUmlPackage1()
            addClassifiersToCompilationUnit(listOf(getJavaClass1(), getJavaClass2(), getJavaInterface1()), javaPackage)
            addClassifiersToUmlPackage(listOf(getUmlClass1(), getUmlClass2(), getUmlInterface1()), umlPackage)
            return listOf(javaPackage, umlPackage)
        }

        fun getContainerWith2Packages(): List<EObject> {
            val javaPackage1 = getCompilationUnit1()
            val javaPackage2 = getCompilationUnit2()
            val umlPackage1 = getUmlPackage1()
            val umlPackage2 = getUmlPackage2()
            addClassifiersToCompilationUnit(listOf(getJavaClass1()), javaPackage1)
            addClassifiersToCompilationUnit(listOf(getJavaClass2()), javaPackage2)
            addClassifiersToUmlPackage(listOf(getUmlClass1()), umlPackage1)
            addClassifiersToUmlPackage(listOf(getUmlClass2()), umlPackage2)
            return listOf(javaPackage1, javaPackage2, umlPackage1, umlPackage2)
        }

        /**
         * Returns a container with a Java and Uml Class, only containing attributes.
         */
        fun getContainer1(): List<EObject> {
            return listOf(getJavaClass1(), getUmlClass1())
        }

        /**
         * Returns a container with a Java and Uml Class, containing attributes and methods.
         */
        fun getContainer2(): List<EObject> {
            return listOf(getJavaClass1(), getJavaClass2(), getUmlClass1(), getUmlClass2())
        }

        /**
         * Returns a container with a Java and Uml Class, containing attributes, methods and interfaces.
         */
        fun getContainer3(): List<EObject> {
            return listOf(getJavaClass1(), getJavaClass2(), getJavaInterface1(), getUmlClass1(), getUmlClass2(), getUmlInterface1())
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
            return EResourceMock.mockERessourceForEObject(javaPackage)
        }

        private fun getCompilationUnit2(): CompilationUnit {
            val javaPackage = ContainersFactory.eINSTANCE.createCompilationUnit()
            javaPackage.name = "examplePackage2"
            return EResourceMock.mockERessourceForEObject(javaPackage)
        }

        private fun getUmlPackage1(): Package {
            val umlPackage = UMLFactory.eINSTANCE.createPackage()
            umlPackage.name = "examplePackage"
            return EResourceMock.mockERessourceForEObject(umlPackage)
        }

        private fun getUmlPackage2(): Package {
            val umlPackage = UMLFactory.eINSTANCE.createPackage()
            umlPackage.name = "examplePackage2"
            return EResourceMock.mockERessourceForEObject(umlPackage)
        }

        private fun getJavaClass1(): Class {
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


            return EResourceMock.mockERessourceForEObject(javaClass)
        }

        private fun getJavaClass2(): Class {
            val javaClass = ClassifiersFactory.eINSTANCE.createClass()
            javaClass.name = "Class2"
            javaClass.makePublic()

            val member2 = MembersFactory.eINSTANCE.createField()
            member2.name = "myIntAttribute2"
            val intType2 = TypesFactory.eINSTANCE.createInt()
            member2.typeReference = intType2
            javaClass.members.add(EResourceMock.mockERessourceForEObject(member2))

            val member3 = MembersFactory.eINSTANCE.createField()
            member3.name = "myIntAttribute3"
            val intType3 = TypesFactory.eINSTANCE.createInt()
            member3.typeReference = intType3
            javaClass.members.add(EResourceMock.mockERessourceForEObject(member3))



            val method = MembersFactory.eINSTANCE.createClassMethod()
            method.name = "myMethod"
            method.makePublic()
            method.typeReference = TypesFactory.eINSTANCE.createInt()
            val parameter = ParametersFactory.eINSTANCE.createCatchParameter()
            parameter.name = "myParameter"
            parameter.typeReference = TypesFactory.eINSTANCE.createInt()
            method.parameters.add(EResourceMock.mockERessourceForEObject(parameter))

            val block = StatementsFactory.eINSTANCE.createBlock()
            val statement = StatementsFactory.eINSTANCE.createReturn()
            val value = LiteralsFactory.eINSTANCE.createDecimalIntegerLiteral()
            value.decimalValue = BigInteger.valueOf(5)
            statement.returnValue = value
            method.statement = block
            method.block.statements.add(statement)
            javaClass.members.add(EResourceMock.mockERessourceForEObject(method))

            return EResourceMock.mockERessourceForEObject(javaClass)
        }

        private fun getJavaInterface1(): Interface {
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
            method.parameters.add(EResourceMock.mockERessourceForEObject(parameter))
            method.statement = StatementsFactory.eINSTANCE.createBlock()
            javaInterface.members.add(EResourceMock.mockERessourceForEObject(method))

            return EResourceMock.mockERessourceForEObject(javaInterface)
        }

        private fun getUmlInterface1(): org.eclipse.uml2.uml.Interface {
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
            method.ownedParameters.add(EResourceMock.mockERessourceForEObject(parameter))
            umlInterface.ownedOperations.add(EResourceMock.mockERessourceForEObject(method))
            return EResourceMock.mockERessourceForEObject(umlInterface)
        }

        private fun getUmlClass1(): org.eclipse.uml2.uml.Class {
            val umlClass = UMLFactory.eINSTANCE.createClass()
            umlClass.name = "Class1"
            val attribute = UMLFactory.eINSTANCE.createProperty()
            attribute.name = "myIntAttribute"
            attribute.type = UMLFactory.eINSTANCE.createPrimitiveType()
            attribute.type.name = "int"

            val attribute2 = UMLFactory.eINSTANCE.createProperty()
            attribute2.name = "myBooleanAttribute"
            attribute2.type = UMLFactory.eINSTANCE.createPrimitiveType()
            attribute2.type.name = "boolean"
            umlClass.ownedAttributes.add(EResourceMock.mockERessourceForEObject(attribute))
            umlClass.ownedAttributes.add(EResourceMock.mockERessourceForEObject(attribute2))
            return EResourceMock.mockERessourceForEObject(umlClass)
        }

        private fun getUmlClass2(): org.eclipse.uml2.uml.Class {
            val umlClass = UMLFactory.eINSTANCE.createClass()
            umlClass.name = "Class2"
            val attribute = UMLFactory.eINSTANCE.createProperty()
            attribute.name = "myIntAttribute2"
            attribute.type = UMLFactory.eINSTANCE.createPrimitiveType()
            attribute.type.name = "int"

            val attribute2 = UMLFactory.eINSTANCE.createProperty()
            attribute2.name = "myIntAttribute3"
            attribute2.type = UMLFactory.eINSTANCE.createPrimitiveType()
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
            method.ownedParameters.add(EResourceMock.mockERessourceForEObject(parameter))
            umlClass.ownedOperations.add(EResourceMock.mockERessourceForEObject(method))
            umlClass.ownedAttributes.add(EResourceMock.mockERessourceForEObject(attribute))
            umlClass.ownedAttributes.add(EResourceMock.mockERessourceForEObject(attribute2))
            return EResourceMock.mockERessourceForEObject(umlClass)
        }
    }
}