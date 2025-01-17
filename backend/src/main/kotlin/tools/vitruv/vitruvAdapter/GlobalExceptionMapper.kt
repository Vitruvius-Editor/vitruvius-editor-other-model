package tools.vitruv.vitruvAdapter

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import tools.vitruv.vitruvAdapter.exception.ConnectionNotFoundException

/**
 * This class maps exceptions to HTTP status codes.
 */
@ControllerAdvice
class GlobalExceptionMapper {
    @ExceptionHandler(ConnectionNotFoundException::class)
    fun handleConnectionNotFoundException(e: ConnectionNotFoundException): ResponseEntity<String> {
        return ResponseEntity("Connection not found", HttpStatus.NOT_FOUND)
    }
}