package tools.vitruv.vitruvAdapter.vitruv.api

/**
 * This class is used to map the content of a window to a string and vice versa.
 * @author uhsab
 */
abstract class DisplayContentMapper<E> {


    /**
     * This function is used to parse the content of a window to a string.
     * @param e the content of the window
     * @return the string representation of the content
     */
    abstract fun parseContent(content: E): String

    /**
     * This function is used to parse the string representation of the content to the content itself.
     * @param content the string representation of the content
     * @return the content itself
     */
    abstract fun parseString(content: String): E

    /**
     * This function is used to get the name of the visualizer that is used to display the content.
     * Note that Visualizer and ContentMapper should have a 1:1 relationship.
     * @return the name of the visualizer
     */
    abstract fun getVisualizerName(): String
}
