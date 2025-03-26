package tools.vitruv.vitruvAdapter.core.impl

/**
 * Represents the name of a display view.
 * @param viewName The name of the display view.
 */
enum class DisplayViewName(
    val viewName: String,
) {
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

    /**
     * The name of the package diagram view.
     */
    PACKAGE_DIAGRAM("PackageDiagram"),
    /**
     * The name of the person table view.
     */
    PERSON_TABLE("PersonTable"),

    /**
     * The name of the family diagram view.
     */
    FAMILY_DIAGRAM("FamilyDiagram")
}
