package tools.vitruv.vitruvAdapter.core.api.testutils

import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper

class TestTextDisplayContentMapper : DisplayContentMapper<String> {
    override fun parseString(content: String): String = content

    override fun parseContent(content: String): String = content

    override fun getVisualizerName(): String = "TestVisualizer"
}
