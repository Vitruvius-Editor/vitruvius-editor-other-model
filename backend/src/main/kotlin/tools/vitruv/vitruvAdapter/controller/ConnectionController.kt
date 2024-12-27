package tools.vitruv.vitruvAdapter.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tools.vitruv.vitruvAdapter.dto.*
import tools.vitruv.vitruvAdapter.services.ConnectionService

/**
 * This rest controller handles all requests that deal with the management of connections.
 *
 */
@RestController
@RequestMapping(value = ["/api/v1"])
class ConnectionController {
    @Autowired
    lateinit var connectionService: ConnectionService
    /**
     * This method returns a list of all saved connections.
     *
     * @return The list of all connections.
     */
    @GetMapping("/connections")
    fun getConnections() : List<ConnectionResponse> {
        return listOf()
    }

    /**
     * This method returns the data of a single connection.
     *
     * @param id The id of the connection.
     * @return The content of the connection.
     */
    @GetMapping("/connections/{id}")
    fun getConnection(@PathVariable("id") id: String) : ResponseEntity<ConnectionResponse> {
        return ResponseEntity.ok().build()
    }

    /**
     * This method creates a new connection and returns its data.
     *
     * @param body Information required to create a new connection.
     * @return The content of the new connection.
     */
    @PostMapping("/connection")
    fun createConnection(@RequestBody body: ConnectionCreationRequest): ResponseEntity<ConnectionResponse> {
        return ResponseEntity.ok().build()
    }

    /**
     * This method deletes a connection.
     *
     * @param id The id of the connection to delete.
     * @return An empty return value containing the status code.
     */
    @DeleteMapping("/connection/{id}")
    fun deleteConnection(@PathVariable("id") id: String) : ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

    /**
     * This method edits a connection.
     *
     * @param id The id of the connection to edit
     * @param body The data to edit.
     * @return The new content of the connection.
     */
    @PutMapping("/connection/{id}")
    fun editConnection(@PathVariable("id") id: String, @RequestBody body: ConnectionEditRequest) : ResponseEntity<ConnectionResponse> {
        return ResponseEntity.ok().build()
    }
}