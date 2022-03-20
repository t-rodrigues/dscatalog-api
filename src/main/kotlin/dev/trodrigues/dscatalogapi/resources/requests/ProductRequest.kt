package dev.trodrigues.dscatalogapi.resources.requests

import java.time.LocalDateTime

data class ProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String?,
    val date: LocalDateTime?,
    val categories: Set<ProductCategory>
)

data class ProductCategory(
    val id: Long
)
