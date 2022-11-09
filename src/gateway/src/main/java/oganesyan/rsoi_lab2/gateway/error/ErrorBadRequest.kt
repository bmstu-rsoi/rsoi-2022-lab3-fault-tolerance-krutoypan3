package oganesyan.rsoi_lab2.gateway.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class ErrorBadRequest(override var message: String, errors: ArrayList<Error400Description>) :
    RuntimeException() {
    var errors: List<Error400Description>

    init {
        this.errors = errors
    }
}
