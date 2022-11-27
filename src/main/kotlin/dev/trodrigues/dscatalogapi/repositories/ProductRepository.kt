package dev.trodrigues.dscatalogapi.repositories

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    fun existsByCategories(category: Category): Boolean

    @Query("SELECT p FROM Product p JOIN FETCH p.categories WHERE p.id = :productId")
    override fun findById(productId: Long): Optional<Product>

}
