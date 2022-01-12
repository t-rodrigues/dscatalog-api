package dev.trodrigues.dscatalogapi.resources.response

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.time.ZoneId

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
    val status: Int,
    val error: String? = null,
    val errors: List<FieldErrorResponse>? = null,
    val message: String? = null,
    val path: String? = null
)
