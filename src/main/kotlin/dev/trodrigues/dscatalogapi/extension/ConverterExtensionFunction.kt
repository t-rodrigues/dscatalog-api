package dev.trodrigues.dscatalogapi.extension

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.resources.response.CategoryResponse

fun Category.toResponse(): CategoryResponse = CategoryResponse(
    id = this.id,
    name = this.name
)
