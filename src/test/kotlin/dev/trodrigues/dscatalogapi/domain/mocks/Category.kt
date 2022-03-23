package dev.trodrigues.dscatalogapi.domain.mocks

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.resources.requests.CategoryRequest
import java.util.*

fun buildCategory(id: Long = Random().nextLong(), name: String = "${UUID.randomUUID()}"): Category = Category(
    id = id,
    name = name
)

fun buildCategoryRequest(name: String = "${UUID.randomUUID()}"): CategoryRequest = CategoryRequest(
    name = name
)
