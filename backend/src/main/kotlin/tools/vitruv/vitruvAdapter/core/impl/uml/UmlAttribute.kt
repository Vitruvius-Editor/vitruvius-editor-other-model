package tools.vitruv.vitruvAdapter.core.impl.uml

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class UmlAttribute(
    val uuid: String,
    val visibility: UmlVisibility,
    val name: String,
    val type: UmlType
)