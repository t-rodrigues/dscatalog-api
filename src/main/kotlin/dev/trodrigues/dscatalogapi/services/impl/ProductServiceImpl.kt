package dev.trodrigues.dscatalogapi.services.impl

import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.repositories.ProductRepository
import dev.trodrigues.dscatalogapi.services.ProductService
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
) : ProductService {

    override fun findAll(pageable: Pageable): Page<Product> =
        productRepository.findAll(pageable)

    override fun findById(productId: Long): Product =
        productRepository.findById(productId)
            .orElseThrow { ObjectNotFoundException("Product $productId not found") }

}
