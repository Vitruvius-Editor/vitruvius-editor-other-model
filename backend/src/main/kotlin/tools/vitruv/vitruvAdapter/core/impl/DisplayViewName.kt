package tools.vitruv.vitruvAdapter.core.impl

/**
 * Represents the name of a display view.
 * @param viewName The name of the display view.
 */
enum class DisplayViewName(val viewName: String) {
    /**
     * The name of the class table view.
     */
    CLASS_TABLE("ClassTable"),

    /**
     * The name of the source code view.
     */
    SOURCE_CODE("SourceCode"),

    /**
     * The name of the class diagram view.
     */
    CLASS_DIAGRAM("ClassDiagram"),
}