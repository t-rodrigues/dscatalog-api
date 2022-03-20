package dev.trodrigues.dscatalogapi.repositories

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    fun existsByCategories(category: Category): Boolean

}
