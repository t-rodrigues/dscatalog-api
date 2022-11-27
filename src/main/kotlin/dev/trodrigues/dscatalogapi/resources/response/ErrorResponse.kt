package dev.trodrigues.dscatalogapi.resources.response

import java.time.LocalDateTime
import java.time.ZoneId

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
    val status: Int,
    val error: String? = null,
    val errors: List<FieldErrorResponse>? = null,
    val message: String? = null,
    val path: String? = null
)
