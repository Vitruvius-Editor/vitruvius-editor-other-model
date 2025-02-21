package tools.vitruv.vitruvAdapter.utils

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.PackageableElement
import org.eclipse.uml2.uml.UMLFactory
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier
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
        fun getContainer1(): List<EObject> {
            return listOf(getJavaPackage1(), getUmlPackage1())
        }

        private fun getJavaPackage1(): EObject {
            val javaPackage = ContainersFactory.eINSTANCE.createPackage()
            javaPackage.name = "examplePackage"
            javaPackage.classifiers.add(getJavaClass1() as ConcreteClassifier?)
            return javaPackage
        }

        private fun getUmlPackage1(): EObject {
            val umlPackage = UMLFactory.eINSTANCE.createPackage()
            umlPackage.name = "examplePackage"
            umlPackage.packagedElements.add(getUmlClass1() as PackageableElement?)
            return umlPackage
        }

        /**
         * Returns a container with a Java and Uml Class, containing attributes and methods.
         */
        fun getContainer2(): List<EObject> {
            return listOf(getJavaClass1(), getUmlClass1(), getJavaClass2(), getUmlClass2())
        }

        /**
         * Returns a container with a Java and Uml Class, containing attributes, methods and interfaces.
         */
        fun getContainer3(): List<EObject> {
            return listOf(getJavaClass1(), getUmlClass1(), getJavaClass2(), getUmlClass2(), getJavaInterface1(), getUmlInterface1())
        }

        private fun getJavaClass1(): EObject {
            val javaClass = ClassifiersFactory.eINSTANCE.createClass()
            javaClass.name = "Class1"
            javaClass.makePublic()
            val member = MembersFactory.eINSTANCE.createField()
            member.name = "myIntAttribute"
            javaClass.members.add(member)

            val intType = TypesFactory.eINSTANCE.createInt()

            val booleanType = TypesFactory.eINSTANCE.createBoolean()
            member.typeReference = intType

            val member1 = MembersFactory.eINSTANCE.createField()
            member1.name = "myBooleanAttribute"
            javaClass.members.add(member1)
            member1.typeReference = booleanType
            val initialValue1 = LiteralsFactory.eINSTANCE.createBooleanLiteral()
            initialValue1.isValue = true
            member1.initialValue = initialValue1

            val compilationUnit = ContainersFactory.eINSTANCE.createCompilationUnit()
            compilationUnit.name = "exampleCompilationUnit"
            compilationUnit.classifiers.add(javaClass)
            return javaClass
        }

        private fun getJavaClass2(): EObject {
            val javaClass = ClassifiersFactory.eINSTANCE.createClass()
            javaClass.name = "Class2"
            javaClass.makePublic()

            val member2 = MembersFactory.eINSTANCE.createField()
            member2.name = "myIntAttribute2"
            javaClass.members.add(member2)
            val intType2 = TypesFactory.eINSTANCE.createInt()
            member2.typeReference = intType2

            val member3 = MembersFactory.eINSTANCE.createField()
            member3.name = "myIntAttribute3"
            javaClass.members.add(member3)
            val intType3 = TypesFactory.eINSTANCE.createInt()
            member3.typeReference = intType3

            val initialValue = LiteralsFactory.eINSTANCE.createDecimalIntegerLiteral()
            initialValue.decimalValue = BigInteger.valueOf(5)
            member2.initialValue = initialValue

            val method = MembersFactory.eINSTANCE.createClassMethod()
            method.name = "myMethod"
            method.makePublic()
            method.typeReference = intType2
            val parameter = ParametersFactory.eINSTANCE.createCatchParameter()
            parameter.name = "myParameter"
            parameter.typeReference = intType2
            method.parameters.add(parameter)
            val statement = StatementsFactory.eINSTANCE.createReturn()
            val value = LiteralsFactory.eINSTANCE.createDecimalIntegerLiteral()
            value.decimalValue = BigInteger.valueOf(5)
            statement.returnValue = value
            method.block.statements.add(statement )
            javaClass.members.add(method)


            val javaPackage = ContainersFactory.eINSTANCE.createCompilationUnit()
            javaPackage.name = "exampleCompilationUnit"
            javaPackage.classifiers.add(javaClass)
            return javaPackage
        }

        private fun getJavaInterface1(): EObject {
            val javaInterface = ClassifiersFactory.eINSTANCE.createInterface()
            javaInterface.name = "Interface1"
            javaInterface.makePublic()

            val method = MembersFactory.eINSTANCE.createInterfaceMethod()
            method.name = "myMethod"
            method.makePublic()
            method.typeReference = TypesFactory.eINSTANCE.createInt()
            val parameter = ParametersFactory.eINSTANCE.createCatchParameter()
            parameter.name = "myParameter"
            parameter.typeReference = TypesFactory.eINSTANCE.createInt()
            method.parameters.add(parameter)
            javaInterface.members.add(method)
            val compilationUnit = ContainersFactory.eINSTANCE.createCompilationUnit()
            compilationUnit.name = "exampleCompilationUnit"
            compilationUnit.classifiers.add(javaInterface)
            return javaInterface
        }

        private fun getUmlInterface1(): EObject {
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
            method.ownedParameters.add(parameter)
            umlInterface.ownedOperations.add(method)
            return umlInterface
        }

        private fun getUmlClass1(): EObject {
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
            return umlClass
        }

        private fun getUmlClass2(): EObject {
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
            method.ownedParameters.add(parameter)
            umlClass.ownedOperations.add(method)
            return umlClass
        }

    }
}