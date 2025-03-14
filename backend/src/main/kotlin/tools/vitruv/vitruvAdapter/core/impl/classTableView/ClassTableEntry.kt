package tools.vitruv.vitruvAdapter.core.impl.classTableView

import tools.vitruv.vitruvAdapter.core.impl.ViewRecommendation
import tools.vitruv.vitruvAdapter.core.impl.table.ColumnInfo

/**
 * Represents a single entry in the class table.
 * @param uuid The unique identifier of the class.
 * @param viewRecommendations The view recommendations for the class.
 * @param name The name of the class.
 * @param visibility The visibility of the class.
 * @param isAbstract Whether the class is abstract.
 * @param isFinal Whether the class is final.
 * @param superClassName The name of the superclass.
 * @param interfaces The names of the interfaces the class implements.
 * @param attributeCount The number of attributes the class has.
 * @param methodCount The number of methods the class has.
 * @param linesOfCode The number of lines of code in the class.
 * @see ViewRecommendation
 */
data class ClassTableEntry(
    @ColumnInfo(false, "", false)
    val uuid: String,
    @ColumnInfo(false, "", false)
    val viewRecommendations: List<ViewRecommendation>,
    @ColumnInfo(true, "Name", true)
    val name: String,
    @ColumnInfo(true, "Visibility", true)
    val visibility: String,
    @ColumnInfo(true, "Abstract", false)
    val isAbstract: Boolean,
    @ColumnInfo(true, "Final", false)
    val isFinal: Boolean,
    @ColumnInfo(true, "Superclass", false)
    val superClassName: String,
    @ColumnInfo(true, "Interfaces", false)
    val interfaces: List<String>,
    @ColumnInfo(true, "Attributes", false)
    val attributeCount: Int,
    @ColumnInfo(true, "Methods", false)
    val methodCount: Int,
    @ColumnInfo(true, "Lines of Code", false)
    val linesOfCode: Int,
)
