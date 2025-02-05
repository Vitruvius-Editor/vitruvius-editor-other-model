package tools.vitruv.vitruvAdapter.core.impl.classTableView

import tools.vitruv.vitruvAdapter.core.impl.ViewRecommendation
import tools.vitruv.vitruvAdapter.core.impl.table.ColumnInfo


/**
 * Represents a single entry in the class table.
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
    val linesOfCode : Int,
)