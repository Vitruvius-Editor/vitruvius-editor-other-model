package tools.vitruv.vitruvAdapter.vitruv.api

/**
 * @author uhsab
 */
abstract class DisplayContent(
    val windows: Set<Window>,
) {
    abstract fun createJsonViewInformation(): JsonViewInformation

    abstract fun getName()
}
