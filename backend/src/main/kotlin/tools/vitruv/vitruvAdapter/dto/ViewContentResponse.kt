package tools.vitruv.vitruvAdapter.dto

import org.eclipse.emf.ecore.EObject

/**
 * This data class holds all properties that are returned when requesting the content of a view.
 *
 */
data class ViewContentResponse(val rootObjects: Collection<EObject>) {
}