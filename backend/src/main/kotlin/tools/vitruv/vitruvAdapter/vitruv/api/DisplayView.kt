package tools.vitruv.vitruvAdapter.vitruv.api

/**
 * This interface represents a display view, which can be displayed in the Vitruvius graphical editor.
 * @author uhsab
 */
interface DisplayView {
    val name: String
    val viewTypeName: String
    val viewMapper: ViewMapper<Any?>
    val windowSelector: Selector
    val contentSelector: ContentSelector
}


