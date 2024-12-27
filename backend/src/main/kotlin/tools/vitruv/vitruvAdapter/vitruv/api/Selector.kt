package tools.vitruv.vitruvAdapter.vitruv.api

/**
 * A selector allows to apply a selection to a list of Windows. A selection is a subset of the given input
 *
 */
interface Selector {
    fun applySelection(content: List<Window>): List<Window>
}