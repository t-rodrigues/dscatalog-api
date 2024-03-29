package dev.trodrigues.dscatalogapi.resources

import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.extension.toPageResponse
import dev.trodrigues.dscatalogapi.extension.toResponse
import dev.trodrigues.dscatalogapi.resources.requests.CategoryRequest
import dev.trodrigues.dscatalogapi.resources.response.CategoryResponse
import dev.trodrigues.dscatalogapi.resources.response.PageResponse
import dev.trodrigues.dscatalogapi.services.CategoryService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/categories")
class CategoryResource(
    private val categoryService: CategoryService
) {

    @GetMapping
    fun getCategories(
        @PageableDefault(page = 0, size = 15, sort = ["name"]) pageable: Pageable
    ): PageResponse<CategoryResponse> =
        categoryService.findAll(pageable).map { it.toResponse() }.toPageResponse()

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): CategoryResponse =
        categoryService.findById(id).toResponse()

    @PostMapping
    fun createCategory(@RequestBody @Valid categoryRequest: CategoryRequest): ResponseEntity<CategoryResponse> {
        val category = categoryService.create(categoryRequest.toModel())
        val location = URI("/categories/${category.id!!}")
        return ResponseEntity.created(location).body(category.toResponse())
    }

    @PutMapping("/{categoryId}")
    fun updateCategory(
        @PathVariable categoryId: Long,
        @RequestBody @Valid categoryRequest: CategoryRequest
    ): CategoryResponse {
        return categoryService.update(categoryId, categoryRequest).toResponse()
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCategory(@PathVariable categoryId: Long) {
        categoryService.delete(categoryId)
    }

}
