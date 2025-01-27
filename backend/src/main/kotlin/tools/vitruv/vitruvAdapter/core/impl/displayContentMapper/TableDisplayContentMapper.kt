package tools.vitruv.vitruvAdapter.core.impl.displayContentMapper

import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTable

class TableDisplayContentMapper: DisplayContentMapper<ClassTable> {

    /**
     * This function is used to parse the content of a window to a string.
     * @param content the content of the window
     * @return the string representation of the content
     */
    override fun parseContent(content: ClassTable): String {
        TODO("Not yet implemented")
    }

    /**
     * This function is used to parse the string representation of the content to the content itself.
     * @param content the string representation of the content
     * @return the content itself
     */
    override fun parseString(content: String): ClassTable {
        TODO("Not yet implemented")
    }

    /**
     * This function is used to get the name of the visualizer that is used to display the content.
     * Note that Visualizer and ContentMapper should have a 1:1 relationship.
     * @return the name of the visualizer
     */
    override fun getVisualizerName(): String {
        TODO("Not yet implemented")
    }


}