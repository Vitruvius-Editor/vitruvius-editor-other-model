package tools.vitruv.vitruvAdapter.core.impl.personTableView

import tools.vitruv.vitruvAdapter.core.impl.table.ColumnInfo
import java.util.Date

class PersonTableEntry(
    @ColumnInfo(true, "Full Name", true)
    val fullName: String,
    @ColumnInfo(true, "Birthday", true)
    val birthday: Date,
)
