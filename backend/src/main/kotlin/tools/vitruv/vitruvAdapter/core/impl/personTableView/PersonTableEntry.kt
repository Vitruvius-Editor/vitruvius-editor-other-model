package tools.vitruv.vitruvAdapter.core.impl.personTableView

import tools.vitruv.vitruvAdapter.core.impl.table.ColumnInfo

class PersonTableEntry(
    @ColumnInfo(false, "", false)
    val uuid: String,
    @ColumnInfo(true, "Full Name", true)
    val fullName: String,
    @ColumnInfo(true, "Birthday", true)
    val birthday: String,
)
