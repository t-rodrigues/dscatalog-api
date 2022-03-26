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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/products")
class ProductResource(
    private val productService: ProductService
) {

    @GetMapping
    fun getProducts(
        @PageableDefault(page = 0, size = 15, sort = ["price"]) pageable: Pageable,
        @RequestParam(required = false) categoryId: Long? = null
    ): PageResponse<ProductResponse> {
        return productService.findAll(ProductSpecification.getProducts(categoryId), pageable).map { it.toResponse() }
            .toPageResponse()
    }

    @GetMapping("/{productId}")
    fun getProductById(@PathVariable productId: Long): ProductResponse {
        return productService.findById(productId).toResponse()
    }

    @PostMapping
    fun createProduct(@RequestBody @Valid productRequest: ProductRequest): ResponseEntity<ProductResponse> {
        val product = productService.create(productRequest)
        val location = URI("/products/${product.id}")
        return ResponseEntity.created(location).body(product.toResponse())
    }

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody @Valid productRequest: ProductRequest
    ): ProductResponse {
        val product = productService.update(productId, productRequest)
        return product.toResponse()
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable productId: Long) {
        productService.delete(productId)
    }

}
