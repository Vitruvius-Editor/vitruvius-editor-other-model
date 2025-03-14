package tools.vitruv.vitruvAdapter.core.impl.table

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class ColumnInfo(
    val shouldBeDisplayed: Boolean = true,
    val displayName: String = "",
    val editable: Boolean = false,
)
