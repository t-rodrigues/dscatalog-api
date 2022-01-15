package dev.trodrigues.dscatalogapi.resources.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProductResponse(
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null,
    val categories: List<CategoryResponse> = mutableListOf()
)
