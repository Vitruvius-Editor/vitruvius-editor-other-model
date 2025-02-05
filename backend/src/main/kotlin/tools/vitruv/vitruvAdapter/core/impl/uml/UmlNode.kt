package tools.vitruv.vitruvAdapter.core.impl.uml

import tools.vitruv.vitruvAdapter.core.impl.ViewRecommendation

data class UmlNode(
    val uuid: String,
    val name: String,
    /**
     * The type of the node. This can be a class, an interface, an enumeration, package etc.
     */
    val nodeType: String,
    val attributes: List<UmlAttribute>,
    val methods: List<UmlMethod>,
    val viewRecommendations: List<ViewRecommendation>
)