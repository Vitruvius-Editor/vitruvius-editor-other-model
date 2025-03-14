package tools.vitruv.vitruvAdapter.core.impl.displayContentMapper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TextDisplayContentMapperTest {
    @Test
    fun testParseContent() {
        val textDisplayContentMapper = TextDisplayContentMapper()
        val content = "content"
        assertEquals(content, textDisplayContentMapper.parseContent(content))
    }

    @Test
    fun testParseString() {
        val textDisplayContentMapper = TextDisplayContentMapper()
        val content = "content"
        assertEquals(content, textDisplayContentMapper.parseString(content))
    }

    @Test
    fun testGetVisualizerName() {
        val textDisplayContentMapper = TextDisplayContentMapper()
        assertEquals("TextVisualizer", textDisplayContentMapper.getVisualizerName())
    }
}
