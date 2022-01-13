package dev.trodrigues.dscatalogapi.resources

import dev.trodrigues.dscatalogapi.extension.toPageResponse
import dev.trodrigues.dscatalogapi.extension.toResponse
import dev.trodrigues.dscatalogapi.resources.response.PageResponse
import dev.trodrigues.dscatalogapi.resources.response.ProductResponse
import dev.trodrigues.dscatalogapi.services.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}
