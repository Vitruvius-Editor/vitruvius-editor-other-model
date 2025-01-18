package tools.vitruv.vitruvAdapter

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import tools.vitruv.vitruvAdapter.exception.ConnectionNotFoundException
import tools.vitruv.vitruvAdapter.exception.DisplayViewNotFoundException

/**
 * This class maps exceptions to HTTP status codes.
 */
@ControllerAdvice
class GlobalExceptionMapper {
    @ExceptionHandler(ConnectionNotFoundException::class)
    fun handleConnectionNotFoundException(e: ConnectionNotFoundException): ResponseEntity<String> {
        return ResponseEntity("Connection not found", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DisplayViewNotFoundException::class)
    fun handleDisplayViewNotFoundException(e: DisplayViewNotFoundException): ResponseEntity<String> {
        return ResponseEntity("Display view not found", HttpStatus.NOT_FOUND)
    }
}