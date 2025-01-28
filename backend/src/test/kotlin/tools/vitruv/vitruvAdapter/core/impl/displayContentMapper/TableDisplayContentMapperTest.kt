package tools.vitruv.vitruvAdapter.core.impl.displayContentMapper

import org.junit.jupiter.api.Assertions.*
import tools.vitruv.vitruvAdapter.core.impl.classTableView.ClassTableEntry
import tools.vitruv.vitruvAdapter.core.impl.classTableView.Table

class TableDisplayContentMapperTest {
    @org.junit.jupiter.api.Test
    fun testMap() {
     // Create an instance of the mapper for ClassTableEntry using the factory method
     val tableDisplayContentMapper = TableDisplayContentMapper.create<ClassTableEntry>()

     // Create a Table<ClassTableEntry> with sample data
     val table = Table<ClassTableEntry>(
      entries = setOf(
       ClassTableEntry(
        uuid = "uuid",
        name = "name",
        visibility = "visibility",
        isFinal = true,
        isAbstract = true,
        superClassName = "superClassName",
        interfaces = listOf("interface"),
        attributeCount = 1,
        methodCount = 1,
        linesOfCode = 1
       ),
       ClassTableEntry(
        uuid = "uui2d",
        name = "name",
        visibility = "visibi2lity",
        isFinal = true,
        isAbstract = true,
        superClassName = "superClass2Name",
        interfaces = listOf("interfa2ce"),
        attributeCount = 1,
        methodCount = 1,
        linesOfCode = 1
       )
      )
     )

     // Serialize the table to JSON
     val result = tableDisplayContentMapper.parseContent(table)
     println("Serialized JSON: $result")

     // Deserialize the JSON back to Table<ClassTableEntry>
     val table2: Table<ClassTableEntry> = tableDisplayContentMapper.parseString(result)
     println("Deserialized Table: $table2")

     val table2SerializedAgain = tableDisplayContentMapper.parseContent(table2)
        println("Serialized JSON after deserialization: $table2SerializedAgain")

     // Assert that the original table and the deserialized table are equal
     assertEquals(table, table2)
    }
}