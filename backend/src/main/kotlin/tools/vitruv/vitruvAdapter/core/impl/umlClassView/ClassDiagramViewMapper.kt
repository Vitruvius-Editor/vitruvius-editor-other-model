package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.*
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.DisplayViewName
import tools.vitruv.vitruvAdapter.core.impl.EUtils
import tools.vitruv.vitruvAdapter.core.impl.ViewRecommendation
import tools.vitruv.vitruvAdapter.core.impl.abstractMapper.UmlViewMapper
import tools.vitruv.vitruvAdapter.core.impl.displayContentMapper.UmlDisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.uml.*

/**
 * Maps EObjects from the UML metamodel to class diagrams and vice versa.
 * @see UmlViewMapper
 * @see UmlDiagram
 * @author
 */
class ClassDiagramViewMapper : UmlViewMapper() {

    private fun mapPackageToUmlDiagram(eObject: Package): UmlDiagram {
        val nodes = mutableListOf<UmlNode>()
        val connections = mutableListOf<UmlConnection>()
        val iterator = eObject.eAllContents()
        val resource = eObject.eResource()
        while (iterator.hasNext()) {
            val next = iterator.next()
            val nextURI = resource?.getURIFragment(next) ?: ""
            if (next is Class) {
                val viewRecommendations = mutableListOf<ViewRecommendation>()
                viewRecommendations.add(ViewRecommendation(DisplayViewName.SOURCE_CODE.viewName, next.name))
                val isAbstract = if (next.isAbstract) "<<abstract>>" else "<<class>>"
                val classNode =
                    UmlNode(nextURI, next.name, isAbstract, getUmlAttributes(next), getUmlMethods(next), viewRecommendations.toList())
                nodes.add(classNode)

                if (next.interfaceRealizations.isNotEmpty()) {
                    next.interfaceRealizations.forEach { interfaceRealization ->
                        val sourceURI = resource?.getURIFragment(next) ?: ""
                        val targetURI = resource?.getURIFragment(interfaceRealization) ?: ""
                        val umlConnection = UmlConnection(
                            "$sourceURI$$targetURI",
                            sourceURI,
                            targetURI,
                            UmlConnectionType.IMPLEMENTS,
                            "",
                            "",
                            ""
                        )
                        connections.add(umlConnection)
                    }
                }
                if (next.superClasses.isNotEmpty()) {
                    val superClass = next.superClasses.first()
                    val sourceURI = resource?.getURIFragment(superClass) ?: ""
                    val targetURI = resource?.getURIFragment(next) ?: ""
                    val umlConnection = UmlConnection(
                        "$sourceURI$$targetURI",
                        sourceURI,
                        targetURI,
                        UmlConnectionType.EXTENDS,
                        "",
                        "",
                        ""
                    )
                    connections.add(umlConnection)
                }
            }
            if (next is Interface) {
                val viewRecommendations = mutableListOf<ViewRecommendation>()
                viewRecommendations.add(ViewRecommendation(DisplayViewName.SOURCE_CODE.viewName, next.name))
                val interfaceNode =
                    UmlNode(nextURI, next.name, "<<interface>>", listOf(), getUmlMethods(next), viewRecommendations)
                nodes.add(interfaceNode)

                if (next.redefinedInterfaces.isNotEmpty()) {
                    next.redefinedInterfaces.forEach { redefinedInterface ->
                        val sourceURI = resource?.getURIFragment(redefinedInterface) ?: ""
                        val targetURI = resource?.getURIFragment(next) ?: ""
                        val umlConnection = UmlConnection(
                            "$sourceURI$$targetURI",
                            sourceURI,
                            targetURI,
                            UmlConnectionType.EXTENDS,
                            "",
                            "",
                            ""
                        )
                        connections.add(umlConnection)
                    }
                }
            }
        }
        return UmlDiagram(nodes, connections)
    }


    private fun getUmlAttributes(next: Class): List<UmlAttribute> {
        val umlAttributes = mutableListOf<UmlAttribute>()
        next.ownedAttributes.forEach {
            val visibilitySymbol = getVisibilitySymbol(it.visibility.literal.lowercase())
            val umlVisibility = UmlVisibility.fromSymbol(visibilitySymbol) ?: UmlVisibility.PUBLIC
            umlAttributes.add(
                UmlAttribute(EUtils.getUUIDForEObject(it), umlVisibility, it.name,
                    UmlType(EUtils.getUUIDForEObject(it.type), it.type.name)
                ))
        }
        return umlAttributes
    }

    private fun getUmlMethods(next: Classifier): List<UmlMethod> {
        val umlMethods = mutableListOf<UmlMethod>()
        next.operations.forEach {
            val visibilitySymbol = getVisibilitySymbol(it.visibility.literal.lowercase())
            val umlVisibility = UmlVisibility.fromSymbol(visibilitySymbol) ?: UmlVisibility.PUBLIC
            val umlParameters = mutableListOf<UmlParameter>()
            it.ownedParameters.filter { it.direction == ParameterDirectionKind.IN_LITERAL }.forEach { parameter ->
                umlParameters.add(UmlParameter(EUtils.getUUIDForEObject(parameter), parameter.type.name, UmlType(EUtils.getUUIDForEObject(parameter.type), parameter.type.name)))
            }
            umlMethods.add(UmlMethod(it.eResource().getURIFragment(it), umlVisibility, it.name, umlParameters, UmlType(EUtils.getUUIDForEObject(it.type), it.type.name)))
        }
        return umlMethods
    }

    private fun getVisibilitySymbol(visibility: String): String {
        return when (visibility) {
            "public" -> "+"
            "private" -> "-"
            "protected" -> "#"
            "package" -> "~"
            else -> ""
        }
    }


    /**
     * Maps the given view content to a list of windows.
     * @param preMappedWindows the pre-mapped windows to map to windows.
     * @return The windows representing the view content.
     */
    override fun mapEObjectsToWindowsContent(preMappedWindows: List<PreMappedWindow<UmlDiagram>>): List<Window<UmlDiagram>> {
        val windows = mutableListOf<Window<UmlDiagram>>()
        for (preMappedWindow in preMappedWindows) {
            for (eObject in preMappedWindow.neededEObjects) {
                if (eObject !is Package) {
                    continue
                }
                val umlDiagram = mapPackageToUmlDiagram(eObject)
                windows.add(preMappedWindow.createWindow(umlDiagram))
            }
        }

        return windows
    }

    /**
     * Maps the given json string to view content, compares it to [preMappedWindows] and applies the changes to the eObjects of [preMappedWindows].
     * Note that no changes will be applied to the model,
     * this have to be done after this method with a View object, where the [preMappedWindows] came from.
     * @param preMappedWindows The pre-mapped windows to compare the windows to.
     * @param windows the windows to map to EObjects.
     * @return The view content.
     */
    override fun mapWindowsToEObjectsAndApplyChangesToEObjects(
        preMappedWindows: List<PreMappedWindow<UmlDiagram>>,
        windows: List<Window<UmlDiagram>>
    ): List<EObject> {
        val windowPairs = pairWindowsTogether(preMappedWindows, windows)
        for (item in windowPairs) {
            applyChangesToWindow(item.first, item.second)
        }
        return listOf() //unnecessary return value, has to be changed
    }

    private fun applyChangesToWindow(preMappedWindow: PreMappedWindow<UmlDiagram>, window: Window<UmlDiagram>) {
        val checkedClasses = mutableListOf<PackageableElement>()
        for (eObject in preMappedWindow.neededEObjects) {
            if (eObject !is Package) {
                continue
            }
            checkedClasses.addAll(eObject.packagedElements.filterIsInstance<PackageableElement>())
            for (node in window.content.nodes) {
                val umlElement = eObject.eResource()?.getEObject(node.uuid)
                if (umlElement == null) {
                    if (node.nodeType == "<<class>>") {
                        val newClass = eObject.createOwnedClass(node.name, false)
                        newClass.name = node.name
                        node.attributes.forEach { createAttributeForUmlAttributeInClass(newClass, it) }
                        node.methods.forEach { createMethodFromUmlMethodInClass(newClass, it) }
                        eObject.packagedElements.add(newClass)
                    }else if(node.nodeType == "<<interface>>"){
                        val newInterface = eObject.createOwnedInterface(node.name)
                        newInterface.name = node.name
                        node.methods.forEach { createMethodFromUmlMethodInInterface(newInterface, it) }
                        eObject.packagedElements.add(newInterface)
                    }
                    continue
                }
                checkedClasses.remove(umlElement)
                val connections = findAllConnectionsForSourceCodeUUID(node.uuid, window.content.connections)
                if (umlElement is Class) {
                    if (umlElement.name != node.name) {
                        umlElement.name = node.name
                    }
                    for (umlAttribute in node.attributes) {
                        val attribute = umlElement.eResource().getEObject(umlAttribute.uuid)
                        if (attribute == null) {
                            createAttributeForUmlAttributeInClass(umlElement, umlAttribute)
                        } else {
                            if (attribute is Property) {
                                editAttributeProperties(attribute, umlAttribute, umlElement)
                            } else {
                                throw IllegalStateException("The attribute with the UUID ${umlAttribute.uuid} could not be linked to a attribute.")
                            }
                        }
                    }

                    for (method in node.methods) {
                        val operation = umlElement.eResource().getEObject(method.uuid)

                        if (operation == null) {
                            createMethodFromUmlMethodInClass(umlElement, method)
                        } else {
                            if (operation is Operation) {
                                editOperationProperties(operation, method, umlElement)
                            } else {
                                throw IllegalStateException("The operation with the UUID ${method.uuid} could not be linked to a operation.")
                            }
                        }
                    }
                    if (umlElement.superClasses.isNotEmpty()) {
                        val extendsConnection = connections.find { it.connectionType == UmlConnectionType.EXTENDS }
                        if (extendsConnection == null) {
                            umlElement.superClasses.clear()
                        }
                    }
                    for (interfaceRealization in umlElement.interfaceRealizations) {
                        val implementsConnection = connections.find {
                            it.connectionType == UmlConnectionType.IMPLEMENTS
                                    && it.targetNodeUUID == interfaceRealization.eResource().getURIFragment(interfaceRealization.contract) }
                        if (implementsConnection == null) {
                            interfaceRealization.destroy()
                        }
                    }
                } else if (umlElement is Interface) {
                    if (umlElement.name != node.name) {
                        umlElement.name = node.name
                    }
                    for (methods in node.methods) {
                        val umlOperation = umlElement.operations.find { it.name == methods.name }
                        if (umlOperation == null) {
                            createMethodFromUmlMethodInInterface(umlElement, methods)
                        } else {
                            editOperationProperties(umlOperation, methods, umlElement)
                        }
                    }
                }

                for (connection in connections) {
                    if (connection.targetNodeUUID == "" || connection.sourceNodeUUID == "") {
                        throw IllegalStateException("The target node UUID of a connection of type EXTENDS is empty." +
                                "You cannot add connections between nodes wich have not been created in the model yet.")
                    }
                    val sourceClass = eObject.eResource()?.getEObject(connection.sourceNodeUUID)
                    val targetClass = eObject.eResource()?.getEObject(connection.targetNodeUUID)

                    if (sourceClass == null || targetClass == null) {
                        throw IllegalStateException("The source or target class of a connection could not be found in the model.")
                    }

                    if (connection.connectionType == UmlConnectionType.EXTENDS) {
                        if (targetClass is Class && sourceClass is Class) {
                            sourceClass.createGeneralization(targetClass)
                        } else if (targetClass is Interface && sourceClass is Interface) {
                            sourceClass.createGeneralization(targetClass)
                        } else {
                            throw IllegalStateException("Extends can only be used between two classes or interfaces.")
                        }
                    } else if (connection.connectionType == UmlConnectionType.IMPLEMENTS) {
                        if (sourceClass is Class && targetClass is Interface) {
                            val alreadyImplemented = sourceClass.interfaceRealizations.any { it.contract == targetClass }

                            if (!alreadyImplemented) {
                                // Create the interface realization only if it does not exist.
                                sourceClass.createInterfaceRealization(connection.connectionName, targetClass)
                            }

                        } else {
                            throw IllegalStateException("Implements can only be used between a class and an interface.")
                        }
                    } else {
                        throw IllegalStateException("The connection type ${connection.connectionType} is not supported.")
                    }
                }


            }
        }
        for (checkedClass in checkedClasses) {
            if (checkedClass is Class || checkedClass is Interface) {
                checkedClass.destroy()
            }
        }
    }

    private fun editAttributeProperties(
        attribute: Property,
        umlAttribute: UmlAttribute,
        umlElement: Class
    ) {
        editVisibility(attribute, umlAttribute.visibility)
        if (attribute.name != umlAttribute.name) {
            attribute.name = umlAttribute.name
        }
        if (EUtils.getUUIDForEObject(attribute.type) != umlAttribute.type.uuid) {
            val type = getTypeOrPrimitiveType(umlAttribute.type.name, umlElement.`package`)
            attribute.type = type
        }
    }

    private fun editVisibility(
        namedElement: NamedElement,
        visibility: UmlVisibility
    ) {
        if (namedElement.visibility.literal.lowercase() != visibility.symbol) {
            namedElement.visibility = VisibilityKind.get(visibility.symbol)
        }
    }

    private fun editOperationProperties(
        operation: Operation,
        umlMethod: UmlMethod,
        classifier: Classifier
    ) {
        editVisibility(operation, umlMethod.visibility)
        if (operation.name != umlMethod.name) {
            operation.name = umlMethod.name
        }
        if (operation.type != getTypeOrPrimitiveType(umlMethod.returnType.name, classifier.`package`)) {
            val type = getTypeOrPrimitiveType(umlMethod.returnType.name, classifier.`package`)
            operation.type = type
        }
        for (parameter in umlMethod.parameters) {
            val ownedParameter = operation.ownedParameters.find { it.name == parameter.name }
            if (ownedParameter == null) {
                operation.createOwnedParameter(parameter.name, getTypeOrPrimitiveType(parameter.type.name, classifier.`package`))
            } else {
                if (ownedParameter.type != getTypeOrPrimitiveType(parameter.type.name, classifier.`package`)) {
                    ownedParameter.type = getTypeOrPrimitiveType(parameter.type.name, classifier.`package`)
                }
            }
        }
    }

    private fun createMethodFromUmlMethodInClass(
        umlClass: Class,
        umlMethod: UmlMethod
    ) {
        val newOperation = umlClass.createOwnedOperation(umlMethod.name, null, null, null)
        newOperation.name = umlMethod.name
        val type = getType(umlMethod.returnType.name, umlClass.`package`)
        newOperation.type = type
        for (parameter in umlMethod.parameters) {
            newOperation.createOwnedParameter(
                parameter.name,
                getTypeOrPrimitiveType(parameter.type.name, umlClass.`package`)
            )
        }

    }


    private fun createMethodFromUmlMethodInInterface(
        umlInterface: Interface,
        methods: UmlMethod
    ) {
        val newOperation = umlInterface.createOwnedOperation(methods.name, null, null, null)
        newOperation.name = methods.name

        val type = getTypeOrPrimitiveType(methods.returnType.name, umlInterface.`package`)
        newOperation.type = type
    }

    private fun createAttributeForUmlAttributeInClass(umlClass: Class, umlAttribute: UmlAttribute) {
        val newAttribute = umlClass.createOwnedAttribute(umlAttribute.name, null)
        newAttribute.name = umlAttribute.name
        newAttribute.type = getTypeOrPrimitiveType(umlAttribute.type.name, umlClass.`package`)
    }

    private fun findAllConnectionsForSourceCodeUUID(sourceCodeUUID: String, connections: List<UmlConnection>): List<UmlConnection> {
        return connections.filter { it.sourceNodeUUID == sourceCodeUUID }
    }

    private fun getTypeOrPrimitiveType(typeName: String, umlPackage: Package): Type{
        val type = getType(typeName, umlPackage)
        if(type != null){
            return type
        }
        return createPrimitiveTypeInPackage(umlPackage, typeName)
    }

    private fun createPrimitiveTypeInPackage(umlPackage: Package, typeName: String): Type{
        val type = UMLFactory.eINSTANCE.createPrimitiveType()
        type.name = typeName
        umlPackage.packagedElements.add(type)
        return type
    }

    private fun getType(typeName: String, umlPackage: Package): Type? {
        val type = umlPackage.getOwnedType(typeName)
        return type
    }




    /**
     * Maps the given view to all windows it can find within the view.
     * @param rootObjects The view to map.
     * @return The names of the windows that are available in the view.
     */
    override fun mapViewToWindows(rootObjects: List<EObject>): Set<String> {
        val windows = mutableSetOf<String>()
        for (rootObject in rootObjects) {
            val iterator = rootObject.eAllContents()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next is Package) {
                    windows.add(next.name)
                }
            }
            if(rootObject is Package){
                windows.add(rootObject.name)
            }
        }
        return windows
    }

    /**
     * Gets the display content of this view mapper, which is able to map the view content to a json string and vice versa.
     * @return The display content of this view mapper.
     */
    override fun getDisplayContent(): DisplayContentMapper<UmlDiagram> {
        return UmlDisplayContentMapper()
    }

}
