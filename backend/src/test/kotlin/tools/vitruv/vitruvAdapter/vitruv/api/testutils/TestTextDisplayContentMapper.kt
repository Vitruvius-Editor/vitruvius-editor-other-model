package tools.vitruv.vitruvAdapter.vitruv.api.testutils

import tools.vitruv.vitruvAdapter.vitruv.api.DisplayContentMapper

class TestTextDisplayContentMapper: DisplayContentMapper<String>() {
    override fun parseString(e: String): String {
        return e
    }

    override fun parseContent(content: String): String {
        return content
    }

    override fun getVisualizerName(): String {
        return "TestVisualizer"
    }
}