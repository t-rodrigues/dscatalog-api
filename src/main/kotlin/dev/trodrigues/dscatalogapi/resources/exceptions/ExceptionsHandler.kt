package dev.trodrigues.dscatalogapi.resources.exceptions

import dev.trodrigues.dscatalogapi.enums.Errors
import dev.trodrigues.dscatalogapi.resources.response.ErrorResponse
import dev.trodrigues.dscatalogapi.resources.response.FieldErrorResponse
import dev.trodrigues.dscatalogapi.services.exceptions.DomainException
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionsHandler {

    @ExceptionHandler(ObjectNotFoundException::class)
    fun handleObjectNotFoundException(ex: ObjectNotFoundException): ResponseEntity<ErrorResponse> =
        generateErrorResponse(
            status = Errors.NOT_FOUND.statusCode,
            error = Errors.NOT_FOUND.error,
            message = ex.message
        )

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ErrorResponse> =
        generateErrorResponse(
            status = Errors.BAD_REQUEST.statusCode,
            error = Errors.BAD_REQUEST.error,
            message = ex.message
        )

    private fun generateErrorResponse(
        status: HttpStatus,
        error: String? = null,
        message: String? = null,
        path: String? = null,
        errors: List<FieldError>? = null
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = error,
            message = message,
            path = path,
            errors = errors?.map { FieldErrorResponse(it.field, it.defaultMessage.toString()) }
        )

        return ResponseEntity.status(status).body(errorResponse)
    }

}
