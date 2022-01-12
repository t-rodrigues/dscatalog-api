package dev.trodrigues.dscatalogapi.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tb_category")
data class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,

    @ManyToMany(mappedBy = "categories")
    @JsonIgnoreProperties("categories")
    val products: Set<Product> = mutableSetOf()

)
