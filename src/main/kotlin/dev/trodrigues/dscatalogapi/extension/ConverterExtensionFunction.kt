package dev.trodrigues.dscatalogapi.extension

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.resources.requests.CategoryRequest
import dev.trodrigues.dscatalogapi.resources.requests.ProductRequest
import dev.trodrigues.dscatalogapi.resources.response.CategoryResponse
import dev.trodrigues.dscatalogapi.resources.response.PageResponse
import dev.trodrigues.dscatalogapi.resources.response.ProductResponse
import org.springframework.data.domain.Page

fun Category.toResponse(): CategoryResponse = CategoryResponse(
    id = this.id!!,
    name = this.name
)

fun CategoryRequest.toModel(): Category = Category(
    name = this.name
)

fun CategoryRequest.toModel(oldCategory: Category): Category = oldCategory.copy(
    name = this.name
)

fun <T> Page<T>.toPageResponse(): PageResponse<T> = PageResponse(
    items = this.content,
    currentPage = this.number,
    totalPages = this.totalPages,
    totalItems = this.totalElements
)

fun ProductRequest.toModel(categories: List<Category>): Product = Product(
    name = this.name,
    description = this.description,
    price = this.price,
    categories = categories,
    imageUrl = this.imageUrl,
    date = this.date
)

fun ProductRequest.toModel(oldProduct: Product, categories: List<Category>): Product = oldProduct.copy(
    name = this.name,
    description = this.description,
    price = this.price,
    categories = categories,
    imageUrl = this.imageUrl,
    date = this.date
)

fun Product.toResponse(): ProductResponse = ProductResponse(
    id = this.id!!,
    name = this.name,
    description = this.description,
    price = this.price,
    imageUrl = this.imageUrl,
    date = this.date,
    categories = this.categories.map { it.toResponse() }.toSet()
)
