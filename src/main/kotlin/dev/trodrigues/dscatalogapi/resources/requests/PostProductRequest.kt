package dev.trodrigues.dscatalogapi.resources.requests

import java.time.LocalDateTime

data class PostProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String?,
    val date: LocalDateTime?,
    val categories: Set<PostProductCategory> = HashSet()
)
