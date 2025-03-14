package tools.vitruv.vitruvAdapter.core.impl

/**
 * Enum class for the different visualizer types.
 * These are the visualizers that the Vitruvius graphical editor supports.
 * @param visualizerName The name of the visualizer.
 */
enum class VisualizerType(
    val visualizerName: String,
) {
    /**
     * The name of the class diagram visualizer.
     */
    TEXT_VISUALIZER("TextVisualizer"),

    /**
     * The name of the table visualizer.
     */
    TABLE_VISUALIZER("TableVisualizer"),

    /**
     * The name of the UML visualizer.
     */
    UML_VISUALIZER("UmlVisualizer"),
}
