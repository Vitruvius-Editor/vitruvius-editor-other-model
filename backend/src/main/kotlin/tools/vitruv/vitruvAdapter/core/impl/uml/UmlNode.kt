package tools.vitruv.vitruvAdapter.core.impl.uml

import tools.vitruv.vitruvAdapter.core.impl.ViewRecommendation

/**
 * Represents a UML node. This might be a class, an interface, package etc.
 * @param uuid The unique identifier of the node
 * @param name The name of the node
 * @param nodeType The type of the node, This can be a class, an interface, an enumeration, package etc.
 * @param attributes The attributes of the node
 * @param methods The methods of the node
 * @param viewRecommendations The view recommendations for the node
 * @see UmlAttribute
 */
data class UmlNode(
    val uuid: String,
    val name: String,
    val nodeType: String,
    val attributes: List<UmlAttribute>,
    val methods: List<UmlMethod>,
    val viewRecommendations: List<ViewRecommendation>,
)
