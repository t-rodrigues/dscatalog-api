package dev.trodrigues.dscatalogapi.resources.response

import java.time.LocalDateTime

data class ProductResponse(
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null,
    val date: LocalDateTime? = null,
    val categories: Set<CategoryResponse>? = null
)
