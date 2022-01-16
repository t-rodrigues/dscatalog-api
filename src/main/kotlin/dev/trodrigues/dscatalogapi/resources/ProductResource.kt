package dev.trodrigues.dscatalogapi.resources

import dev.trodrigues.dscatalogapi.extension.toPageResponse
import dev.trodrigues.dscatalogapi.extension.toResponse
import dev.trodrigues.dscatalogapi.resources.requests.PostProductRequest
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
        @PageableDefault(page = 0, size = 15, sort = ["name"]) pageable: Pageable
    ): PageResponse<ProductResponse> {
        return productService.findAll(pageable).map { it.toResponse() }.toPageResponse()
    }

    @GetMapping("/{productId}")
    fun getProductById(@PathVariable productId: Long): ProductResponse {
        return productService.findById(productId).toResponse()
    }

    @PostMapping
    fun createProduct(@RequestBody request: PostProductRequest): ProductResponse {
        return productService.create(request).toResponse()
    }

}
