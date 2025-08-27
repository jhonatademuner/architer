package com.architer.shared.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
//import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val path = (request as ServletWebRequest).request.requestURI
        log.warn { "Resource not found at $path: ${ex.message}" }

        val errorResponse = ErrorMessage(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message,
            path = path
        )

        return ResponseEntity(errorResponse, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BadCredentialsException::class, PasswordStrengthException::class)
    fun handleAuthException(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val path = (request as ServletWebRequest).request.requestURI
        log.error { "Authentication exception at $path: ${ex.message}" }

        val errorResponse = ErrorMessage(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.UNAUTHORIZED.value(),
            error = HttpStatus.UNAUTHORIZED.reasonPhrase,
            message = ex.message,
            path = path
        )

        return ResponseEntity(errorResponse, HttpHeaders(), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val path = (request as ServletWebRequest).request.requestURI
        log.error { "Unhandled exception at $path: ${ex.message}" }

        val errorResponse = ErrorMessage(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = ex.message,
            path = path
        )

        return ResponseEntity(errorResponse, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
