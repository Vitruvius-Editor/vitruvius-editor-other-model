package tools.vitruv.vitruvAdapter.dto

import java.util.*

/**
 * This data class holds all metadata provided by a view.
 *
 * @property uuid The uuid of the view.
 * @property name The name of the view.
 * @property isClosed Whether the view is closed.
 * @property isModified Whether the view is modified.
 * @property isOutdated Whether the view is outdated.
 */
data class ViewResponse(val uuid: UUID, val name: String, val isClosed: Boolean, val isModified: Boolean, val isOutdated: Boolean) {
}