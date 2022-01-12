package dev.trodrigues.dscatalogapi.resources

import dev.trodrigues.dscatalogapi.extension.toResponse
import dev.trodrigues.dscatalogapi.resources.response.CategoryResponse
import dev.trodrigues.dscatalogapi.services.CategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}
