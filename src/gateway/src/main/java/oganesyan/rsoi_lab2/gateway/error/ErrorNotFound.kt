package oganesyan.rsoi_lab2.gateway.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ErrorNotFound(override var message: String) : RuntimeException()
