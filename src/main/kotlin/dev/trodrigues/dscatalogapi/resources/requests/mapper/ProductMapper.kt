package dev.trodrigues.dscatalogapi.resources.requests.mapper

import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.resources.requests.PostProductRequest
import dev.trodrigues.dscatalogapi.services.CategoryService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ProductMapper(
    private val categoryService: CategoryService
) {

    fun toModel(postProductRequest: PostProductRequest): Product {
        val ids = postProductRequest.categories.map { it.id }
        val categories = categoryService.findAllById(ids)

        if (categories.isEmpty()) {
            throw IllegalArgumentException("sldfgjk")
        }
        println(categories.isEmpty())
        return Product(
            name = postProductRequest.name,
            description = postProductRequest.description,
            price = postProductRequest.price,
            categories = categories,
            date = LocalDateTime.now()
        )
    }

}
