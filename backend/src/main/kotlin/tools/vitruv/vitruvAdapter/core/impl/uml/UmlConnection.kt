package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class UmlConnection(
    val uuid: String,
    val sourceNodeUUID: String,
    val targetNodeUUID: String,
    val connectionType: UmlConnectionType,
    val sourceMultiplicity: String,
    val targetMultiplicity: String,
    val connectionName: String
)