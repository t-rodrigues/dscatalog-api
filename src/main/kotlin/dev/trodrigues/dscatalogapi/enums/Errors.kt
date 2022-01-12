package dev.trodrigues.dscatalogapi.enums

import org.springframework.http.HttpStatus

enum class Errors(val statusCode: HttpStatus, val error: String) {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found")

}
