package dev.trodrigues.dscatalogapi.resources.requests

import javax.validation.constraints.NotBlank

data class CategoryRequest(
    @field:NotBlank
    val name: String
)
