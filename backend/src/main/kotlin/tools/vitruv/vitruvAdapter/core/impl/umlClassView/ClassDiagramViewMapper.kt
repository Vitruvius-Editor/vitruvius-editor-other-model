package tools.vitruv.vitruvAdapter.core.impl.umlClassView

import org.eclipse.emf.ecore.EObject
import org.eclipse.uml2.uml.*
import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.api.PreMappedWindow
import tools.vitruv.vitruvAdapter.core.api.Window
import tools.vitruv.vitruvAdapter.core.impl.DisplayViewName
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
            umlAttributes.add(UmlAttribute(umlVisibility, it.name?:"", it.type?.name?: ""))
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
                umlParameters.add(UmlParameter(parameter.name ?:"", parameter.type?.name ?:""))
            }
            umlMethods.add(UmlMethod(umlVisibility, it.name ?:"", umlParameters, it.type?.name?:""))
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
        val checkedClasses = mutableListOf<Class>()
        for (eObject in preMappedWindow.neededEObjects) {
            if (eObject !is Package) {
                continue
            }
            checkedClasses.addAll(eObject.packagedElements.filterIsInstance<Class>())
            for (node in window.content.nodes) {
                val umlClass = eObject.eResource()?.getEObject(node.uuid) as Class?
                if (umlClass == null) {
                    val newClass = eObject.createOwnedClass(node.name, false)
                    newClass.name = node.name
                    node.attributes.forEach() { createAttributeForUmlAttributeInClass(newClass, it) }
                    node.methods.forEach() { createMethodFromUmlMethodInClass(newClass, it) }
                    eObject.packagedElements.add(newClass)
                    continue
                }
                checkedClasses.remove(umlClass)
                if (umlClass.name != node.name) {
                    umlClass.name = node.name
                }
                for (attribute in node.attributes) {
                    val umlAttribute = umlClass.ownedAttributes.find { it.name == attribute.name }
                    if (umlAttribute == null) {
                        createAttributeForUmlAttributeInClass(umlClass, attribute)
                    } else {
                        if (umlAttribute.visibility.literal.lowercase() != attribute.visibility.symbol) {
                            umlAttribute.visibility = VisibilityKind.get(attribute.visibility.symbol)
                        }
                        if (umlAttribute.name != attribute.name) {
                            umlAttribute.name = attribute.name
                        }
                        if (umlAttribute.type.name != attribute.type) {
                            umlAttribute.type = umlClass.model.getOwnedType(attribute.type)
                        }
                    }
                }

                for (methods in node.methods) {
                    val umlOperation = umlClass.operations.find { it.name == methods.name }
                    if (umlOperation == null) {
                        createMethodFromUmlMethodInClass(umlClass, methods)
                    } else {
                        if (umlOperation.visibility.literal.lowercase() != methods.visibility.symbol) {
                            umlOperation.visibility = VisibilityKind.get(methods.visibility.symbol)
                        }
                        if (umlOperation.name != methods.name) {
                            umlOperation.name = methods.name
                        }
                        if (umlOperation.type.name != methods.returnType) {
                            umlOperation.type = umlClass.model.getOwnedType(methods.returnType)
                        }
                    }
                }
                val connections = findAllConnectionsForSourceCodeUUID(node.uuid, window.content.connections)
                if (umlClass.isAbstract) {
                    val extendsConnection = connections.find { it.connectionType == UmlConnectionType.EXTENDS }
                    if (extendsConnection == null) {
                        umlClass.superClasses.clear()
                    }
                }
                for (interfaceRealization in umlClass.interfaceRealizations) {
                    val implementsConnection = connections.find {
                        it.connectionType == UmlConnectionType.IMPLEMENTS
                                && it.targetNodeUUID == interfaceRealization.eResource().getURIFragment(interfaceRealization.contract) }
                    if (implementsConnection == null) {
                        interfaceRealization.destroy()
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
                            sourceClass.superClasses.clear()
                            sourceClass.createGeneralization(targetClass)
                        } else if (targetClass is Interface && sourceClass is Interface) {
                            sourceClass.redefinedInterfaces.clear()
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
            checkedClass.destroy()
        }
    }

    private fun createMethodFromUmlMethodInClass(
        umlClass: Class,
        methods: UmlMethod
    ) {
        val newOperation = umlClass.createOwnedOperation(methods.name, null, null, null)
        newOperation.name = methods.name
        newOperation.type = umlClass.model.getOwnedType(methods.returnType)
        umlClass.operations.add(newOperation)
    }

    private fun createAttributeForUmlAttributeInClass(umlClass: Class, umlAttribute: UmlAttribute) {
        val newAttribute = umlClass.createOwnedPort(umlAttribute.name, null)
        newAttribute.name = umlAttribute.name
        var type = umlClass.model?.getOwnedType(umlAttribute.type)
        if (type == null) {
            type = UMLFactory.eINSTANCE.createPrimitiveType()
            type.name = umlAttribute.type
        }
        newAttribute.type = type
        umlClass.ownedAttributes.add(newAttribute)
    }

    private fun findAllConnectionsForSourceCodeUUID(sourceCodeUUID: String, connections: List<UmlConnection>): List<UmlConnection> {
        return connections.filter { it.sourceNodeUUID == sourceCodeUUID }
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