package tools.vitruv.vitruvAdapter.vitruv.api

/**
 * @author uhsab
 */
abstract class DisplayContent(
    val name: String
) {
    abstract fun createJsonViewInformation(): JsonViewInformation
}