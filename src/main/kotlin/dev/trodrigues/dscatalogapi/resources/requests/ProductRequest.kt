package dev.trodrigues.dscatalogapi.resources.requests

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class ProductRequest(
    @field:NotBlank
    @field:Size(min = 5, max = 10)
    val name: String,
    @field:NotBlank
    val description: String,
    @field:DecimalMin("0.00")
    val price: Double,
    val imageUrl: String?,
    val date: LocalDateTime?,
    @field:NotNull
    val categories: Set<ProductCategory>
)

data class ProductCategory(
    val id: Long
)
