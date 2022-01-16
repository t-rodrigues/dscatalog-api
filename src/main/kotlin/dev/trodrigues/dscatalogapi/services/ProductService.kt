package dev.trodrigues.dscatalogapi.services

import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.resources.requests.PostProductRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductService {

    fun findAll(pageable: Pageable): Page<Product>

    fun findById(productId: Long): Product

    fun create(postProductRequest: PostProductRequest): Product

}
