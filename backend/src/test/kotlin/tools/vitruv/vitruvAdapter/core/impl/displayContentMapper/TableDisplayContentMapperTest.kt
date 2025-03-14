package tools.vitruv.vitruvAdapter.core.impl.displayContentMapper

import org.junit.jupiter.api.Assertions.*
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableEntry
import tools.vitruv.vitruvAdapter.core.impl.table.TableDTO

class TableDisplayContentMapperTest {
    @org.junit.jupiter.api.Test
    fun testMap() {
        // Create an instance of the mapper for ClassTableEntry using the factory method
        val tableDisplayContentMapper = TableDisplayContentMapper.create<ClassTableEntry>()

        // Create a Table<ClassTableEntry> with sample data
        val tableDto =
            TableDTO.buildTableDTO(
                listOf(
                    ClassTableEntry(
                        uuid = "uuid",
                        viewRecommendations = listOf(),
                        name = "name",
                        visibility = "visibility",
                        isFinal = true,
                        isAbstract = true,
                        superClassName = "superClassName",
                        interfaces = listOf("interface"),
                        attributeCount = 1,
                        methodCount = 1,
                        linesOfCode = 1,
                    ),
                    ClassTableEntry(
                        uuid = "uui2d",
                        viewRecommendations = listOf(),
                        name = "name",
                        visibility = "visibi2lity",
                        isFinal = true,
                        isAbstract = true,
                        superClassName = "superClass2Name",
                        interfaces = listOf("interfa2ce"),
                        attributeCount = 1,
                        methodCount = 1,
                        linesOfCode = 1,
                    ),
                ),
                ClassTableEntry::class,
            )

        // Serialize the table to JSON
        val result = tableDisplayContentMapper.parseContent(tableDto)
        println("Serialized JSON: $result")

        // Deserialize the JSON back to Table<ClassTableEntry>
        val table2: TableDTO<ClassTableEntry> = tableDisplayContentMapper.parseString(result)
        println("Deserialized Table: $table2")

        val table2SerializedAgain = tableDisplayContentMapper.parseContent(table2)
        println("Serialized JSON after deserialization: $table2SerializedAgain")

        assertEquals(result, table2SerializedAgain)
    }
}
