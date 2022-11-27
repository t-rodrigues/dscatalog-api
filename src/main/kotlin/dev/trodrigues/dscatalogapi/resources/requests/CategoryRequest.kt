package dev.trodrigues.dscatalogapi.resources.requests

import jakarta.validation.constraints.NotBlank

data class CategoryRequest(
    @field:NotBlank
    val name: String
)
