package dev.trodrigues.dscatalogapi.resources.exceptions

import dev.trodrigues.dscatalogapi.enums.Errors
import dev.trodrigues.dscatalogapi.services.exceptions.DomainException
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlers {

    @ExceptionHandler(ObjectNotFoundException::class)
    fun handleObjectNotFoundException(ex: ObjectNotFoundException): ResponseEntity<ProblemDetail> =
        generateProblemDatails(
            status = Errors.NOT_FOUND.statusCode,
            error = Errors.NOT_FOUND.error,
            message = ex.message
        )

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ProblemDetail> =
        generateProblemDatails(
            status = Errors.BAD_REQUEST.statusCode,
            error = Errors.BAD_REQUEST.error,
            message = ex.message
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> =
        generateProblemDatails(status = Errors.BAD_REQUEST.statusCode)

    private fun generateProblemDatails(
        status: HttpStatus,
        error: String? = null,
        message: String? = null
    ): ResponseEntity<ProblemDetail> {
        val problemDetail = ProblemDetail.forStatus(status)
        problemDetail.detail = message
        problemDetail.title = error
        return ResponseEntity.status(status).body(problemDetail)
    }

}
