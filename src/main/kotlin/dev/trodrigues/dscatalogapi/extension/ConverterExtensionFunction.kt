package dev.trodrigues.dscatalogapi.extension

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.resources.requests.PostCategoryRequest
import dev.trodrigues.dscatalogapi.resources.requests.PostProductRequest
import dev.trodrigues.dscatalogapi.resources.requests.PutCategoryRequest
import dev.trodrigues.dscatalogapi.resources.response.CategoryResponse
import dev.trodrigues.dscatalogapi.resources.response.PageResponse
import dev.trodrigues.dscatalogapi.resources.response.ProductResponse
import org.springframework.data.domain.Page
import java.time.LocalDateTime

fun Category.toResponse(): CategoryResponse = CategoryResponse(
    id = this.id,
    name = this.name
)

fun PostCategoryRequest.toModel(): Category = Category(
    name = this.name
)

fun PutCategoryRequest.toModel(id: Long): Category = Category(
    id = id,
    name = this.name
)

fun <T> Page<T>.toPageResponse(): PageResponse<T> = PageResponse(
    items = this.content,
    currentPage = this.number,
    totalPages = this.totalPages,
    totalItems = this.totalElements
)

fun Product.toResponse(): ProductResponse = ProductResponse(id = this.id!!,
    name = this.name,
    description = this.description,
    price = this.price,
    imageUrl = this.imageUrl,
    categories = this.categories.map { it.toResponse() })

fun PostProductRequest.toModel(categories: List<Category>): Product = Product(
    name = this.name,
    description = this.description,
    price = this.price,
    categories = categories,
    date = this.date ?: LocalDateTime.now()
)
