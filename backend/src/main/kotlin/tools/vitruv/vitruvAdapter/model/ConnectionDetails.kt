package tools.vitruv.vitruvAdapter.model

import jakarta.persistence.*
import java.util.*

/**
 * This class represents a project stored on a Vitruvius server.
 *
 * @property uuid A unique identifier of the project.
 * @property name Name of the project.
 * @property description A short description of the project.
 * @property url The url to the Vitruvius server of the project.
 */
@Entity
class ConnectionDetails(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val uuid: UUID,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val description: String,
    @Column(nullable = false)
    val url: String,
)
