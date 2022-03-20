package dev.trodrigues.dscatalogapi.resources.response

data class ProductResponse(
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null
)
