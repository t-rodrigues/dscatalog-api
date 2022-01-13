package dev.trodrigues.dscatalogapi.services

import dev.trodrigues.dscatalogapi.domain.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductService {

    fun findAll(pageable: Pageable): Page<Product>

}
