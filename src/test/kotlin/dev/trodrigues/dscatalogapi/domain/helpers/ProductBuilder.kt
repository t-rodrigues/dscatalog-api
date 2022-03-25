package dev.trodrigues.dscatalogapi.domain.helpers

import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.extension.toResponse
import dev.trodrigues.dscatalogapi.resources.requests.ProductCategory
import dev.trodrigues.dscatalogapi.resources.requests.ProductRequest
import dev.trodrigues.dscatalogapi.resources.response.ProductResponse
import java.time.LocalDateTime
import java.util.*

fun buildProduct(
    id: Long = Random().nextLong(),
    name: String = "${UUID.randomUUID()}",
    description: String = "${UUID.randomUUID()}",
    price: Double = Random().nextDouble(),
    categoryId: Long = Random().nextLong()
): Product = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    categories = listOf(
        buildCategory(id = categoryId)
    )
)

fun buildProductRequest(
    name: String = "${UUID.randomUUID()}",
    description: String = "${UUID.randomUUID()}",
    price: Double = Random().nextDouble(),
    categories: Set<Long> = setOf(1, 2),
    imageUrl: String? = null,
    date: LocalDateTime? = null
): ProductRequest = ProductRequest(
    name = name,
    description = description,
    price = price,
    categories = categories.map { ProductCategory(it) }.toSet(),
    imageUrl = imageUrl,
    date = date
)

fun buildProductResponse(
    product: Product = buildProduct()
): ProductResponse = product.toResponse()
