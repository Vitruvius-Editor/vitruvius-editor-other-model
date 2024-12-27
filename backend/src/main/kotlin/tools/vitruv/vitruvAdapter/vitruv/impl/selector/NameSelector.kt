package tools.vitruv.vitruvAdapter.vitruv.impl.selector

import tools.vitruv.vitruvAdapter.vitruv.api.Selector
import tools.vitruv.vitruvAdapter.vitruv.api.Window

/**
 * A selector that selects windows by their name.
 *
 * @property name The name to select by.
 * @property exact Whether the matching should be exact or also a substring
 */
class NameSelector(var name: String, var exact: Boolean): Selector {
    override fun applySelection(content: List<Window>): List<Window> {
        TODO("Not yet implemented")
    }
}