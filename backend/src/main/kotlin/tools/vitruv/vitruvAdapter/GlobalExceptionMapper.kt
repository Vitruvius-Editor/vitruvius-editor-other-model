package tools.vitruv.vitruvAdapter

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import tools.vitruv.vitruvAdapter.exception.ConnectionNotFoundException
import tools.vitruv.vitruvAdapter.exception.DisplayViewNotFoundException
import tools.vitruv.vitruvAdapter.exception.VitruviusConnectFailedException

/**
 * This class maps exceptions to HTTP status codes.
 */
@ControllerAdvice
class GlobalExceptionMapper {
    /**
     * Maps a [ConnectionNotFoundException] to a 404 NOT FOUND response.
     * @param e The exception that was thrown.
     * @return A 404 NOT FOUND response.
     * @see ConnectionNotFoundException
     */
    @ExceptionHandler(ConnectionNotFoundException::class)
    fun handleConnectionNotFoundException(e: ConnectionNotFoundException): ResponseEntity<String> =
        ResponseEntity("Connection not found", HttpStatus.NOT_FOUND)

    /**
     * Maps a [DisplayViewNotFoundException] to a 404 NOT FOUND response.
     * @param e The exception that was thrown.
     * @return A 404 NOT FOUND response.
     * @see DisplayViewNotFoundException
     */
    @ExceptionHandler(DisplayViewNotFoundException::class)
    fun handleDisplayViewNotFoundException(e: DisplayViewNotFoundException): ResponseEntity<String> =
        ResponseEntity("Display view not found", HttpStatus.NOT_FOUND)

    /**
     * Maps a [VitruviusConnectFailedException] to a 502 BAD GATEWAY response.
     * @param e The exception that was thrown.
     * @return A 502 BAD GATEWAY response.
     * @see VitruviusConnectFailedException
     */
    @ExceptionHandler(VitruviusConnectFailedException::class)
    fun handleVitruviusConnectFailedException(e: VitruviusConnectFailedException): ResponseEntity<String> =
        ResponseEntity("Could not connect to Vitruvius", HttpStatus.BAD_GATEWAY)
}
