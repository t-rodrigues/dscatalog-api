package dev.trodrigues.dscatalogapi.services

import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.resources.requests.ProductRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification

interface ProductService {

    fun findAll(spec: Specification<Product>, pageable: Pageable): Page<Product>

    fun findById(productId: Long): Product

    fun create(productRequest: ProductRequest): Product

}
