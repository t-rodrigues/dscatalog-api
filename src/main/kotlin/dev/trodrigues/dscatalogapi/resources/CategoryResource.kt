package dev.trodrigues.dscatalogapi.resources

import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.extension.toResponse
import dev.trodrigues.dscatalogapi.resources.requests.PostCategoryRequest
import dev.trodrigues.dscatalogapi.resources.requests.PutCategoryRequest
import dev.trodrigues.dscatalogapi.resources.response.CategoryResponse
import dev.trodrigues.dscatalogapi.services.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/categories")
class CategoryResource(
    private val categoryService: CategoryService
) {

    @GetMapping
    fun getCategories(): List<CategoryResponse> {
        return categoryService.findAll().map { it.toResponse() }
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): CategoryResponse {
        return categoryService.findById(id).toResponse()
    }

    @PostMapping
    fun createCategory(@RequestBody request: PostCategoryRequest): ResponseEntity<CategoryResponse> {
        val category = categoryService.create(request.toModel())
        val location = URI("/categories/${category.id!!}")
        return ResponseEntity.created(location).body(category.toResponse())
    }

    @PutMapping("/{categoryId}")
    fun updateCategory(@PathVariable categoryId: Long, @RequestBody request: PutCategoryRequest): CategoryResponse {
        return categoryService.update(request.toModel(categoryId)).toResponse()
    }

}
