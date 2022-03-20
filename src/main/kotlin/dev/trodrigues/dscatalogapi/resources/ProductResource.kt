package dev.trodrigues.dscatalogapi.resources

import dev.trodrigues.dscatalogapi.extension.toPageResponse
import dev.trodrigues.dscatalogapi.extension.toResponse
import dev.trodrigues.dscatalogapi.repositories.specifications.ProductSpecification
import dev.trodrigues.dscatalogapi.resources.requests.ProductRequest
import dev.trodrigues.dscatalogapi.resources.response.PageResponse
import dev.trodrigues.dscatalogapi.resources.response.ProductResponse
import dev.trodrigues.dscatalogapi.services.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductResource(
    private val productService: ProductService
) {

    @GetMapping
    fun getProducts(
        @PageableDefault(page = 0, size = 15, sort = ["price"]) pageable: Pageable,
        @RequestParam(required = false) categoryId: Long?
    ): PageResponse<ProductResponse> {
        return productService.findAll(ProductSpecification.getProducts(categoryId), pageable).map { it.toResponse() }.toPageResponse()
    }

    @GetMapping("/{productId}")
    fun getProductById(@PathVariable productId: Long): ProductResponse {
        return productService.findById(productId).toResponse()
    }

    @PostMapping
    fun createProduct(@RequestBody productRequest: ProductRequest): ProductResponse {
        val product = productService.create(productRequest)
        return product.toResponse()
    }

    @PutMapping("/{productId}")
    fun updateProduct(@RequestBody productRequest: ProductRequest) {

    }

}
