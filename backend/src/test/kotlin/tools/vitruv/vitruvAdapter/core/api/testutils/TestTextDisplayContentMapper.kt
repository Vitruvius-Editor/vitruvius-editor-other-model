package tools.vitruv.vitruvAdapter.core.api.testutils

import tools.vitruv.vitruvAdapter.core.api.DisplayContentMapper

class TestTextDisplayContentMapper: DisplayContentMapper<String> {
    override fun parseString(content: String): String {
        return content
    }

    override fun parseContent(content: String): String {
        return content
    }

    override fun getVisualizerName(): String {
        return "TestVisualizer"
    }
}