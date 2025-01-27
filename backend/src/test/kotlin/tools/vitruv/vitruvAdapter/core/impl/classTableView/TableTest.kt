package tools.vitruv.vitruvAdapter.core.impl.classTableView

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TableTest {

    lateinit var entries : Set<ClassTableEntry>

    @BeforeEach
    fun setUp() {
        entries = setOf(
            ClassTableEntry(
                "Test",
                "public",
                false,
                false,
                false,
                "Super",
                listOf("Interface"),
                2,
                3,
                10
            ),
            ClassTableEntry(
                "Test2",
                "private",
                true,
                true,
                true,
                "Super2",
                listOf("Interface2"),
                5,
                7,
                20
            )
        )
    }

    @Test
    fun testSerialization() {
        val objectMapper = ObjectMapper().apply {
         enable(SerializationFeature.INDENT_OUTPUT)
        }
        println(objectMapper.writeValueAsString(entries))
    }

}