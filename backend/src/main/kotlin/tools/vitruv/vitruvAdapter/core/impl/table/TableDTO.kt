package tools.vitruv.vitruvAdapter.core.impl.table

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

data class TableDTO<P>(
    val columns: List<ColumnMetadata>,
    val rows: List<P>,
) {
    companion object {
        fun <P : Any> buildTableDTO(
            rows: List<P>,
            klass: KClass<P>,
        ): TableDTO<P> {
            val columns =
                klass.memberProperties.map { prop ->
                    // Try to get our custom annotation; if not present, use a default
                    val info = prop.findAnnotation<ColumnInfo>()
                    ColumnMetadata(
                        fieldName = prop.name,
                        shouldBeDisplayed = info?.shouldBeDisplayed ?: true,
                        fieldType = prop.returnType.toString(),
                        displayName = info?.displayName?.takeIf { it.isNotEmpty() } ?: prop.name,
                        editable = info?.editable ?: false,
                    )
                }
            return TableDTO(columns, rows)
        }
    }
}
