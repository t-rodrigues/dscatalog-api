package dev.trodrigues.dscatalogapi.services.impl

import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.repositories.ProductRepository
import dev.trodrigues.dscatalogapi.resources.requests.PostProductRequest
import dev.trodrigues.dscatalogapi.services.CategoryService
import dev.trodrigues.dscatalogapi.services.ProductService
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryService: CategoryService,
) : ProductService {

    override fun findAll(pageable: Pageable): Page<Product> = productRepository.findAll(pageable)

    override fun findById(productId: Long): Product {
        return productRepository.findById(productId)
            .orElseThrow { ObjectNotFoundException("Product $productId not found") }
    }

    override fun create(postProductRequest: PostProductRequest): Product {
        val categories = categoryService.findAllById(postProductRequest.categories.map { it.id })
        val product = postProductRequest.toModel(categories)
        return productRepository.save(product)
    }

}
