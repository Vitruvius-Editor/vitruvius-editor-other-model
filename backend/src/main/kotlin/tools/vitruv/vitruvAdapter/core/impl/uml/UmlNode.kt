package tools.vitruv.vitruvAdapter.core.impl.uml

abstract class UmlNode(
    val uuid: String,
    val name: String,
    /**
     * The type of the node. This can be a class, an interface, an enumeration, package etc.
     */
    val nodeType: String,
    val attributes: List<UmlAttribute>,
    val methods: List<String>,
)