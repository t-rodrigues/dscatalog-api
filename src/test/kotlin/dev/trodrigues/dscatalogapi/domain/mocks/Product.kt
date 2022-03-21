package dev.trodrigues.dscatalogapi.domain.mocks

import dev.trodrigues.dscatalogapi.domain.Product
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
        buildCategory(id = categoryId),
        buildCategory(id = categoryId)
    )
)
