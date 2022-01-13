package dev.trodrigues.dscatalogapi.repositories

import dev.trodrigues.dscatalogapi.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

//    @Query("select p from Product p join fetch p.categories")
//    fun findAllWithCategories(): List<Product>

//    @Query("select p FROM Product p join fetch p.categories")
//    fun findByIdWithCategories(id: Long): Product

}
