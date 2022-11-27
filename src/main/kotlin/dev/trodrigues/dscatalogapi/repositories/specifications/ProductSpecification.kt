package dev.trodrigues.dscatalogapi.repositories.specifications

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.domain.Product
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object ProductSpecification {

    fun getProducts(categoryId: Long?): Specification<Product> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            categoryId?.let {
                val productProd = root.join<Product, Category>("categories")
                predicates.add(
                    cb.equal(productProd.get<Long>("id"), categoryId)
                )
            }

            cb.and(*predicates.toTypedArray())
        }
    }

}
