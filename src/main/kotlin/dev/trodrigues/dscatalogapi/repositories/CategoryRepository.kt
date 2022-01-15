package dev.trodrigues.dscatalogapi.repositories

import dev.trodrigues.dscatalogapi.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
}
