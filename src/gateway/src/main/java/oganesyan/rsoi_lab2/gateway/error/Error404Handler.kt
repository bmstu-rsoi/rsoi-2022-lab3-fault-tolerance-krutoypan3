package oganesyan.rsoi_lab2.gateway.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class Error404Handler {
    @ExceptionHandler(ErrorNotFound::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundError(error: ErrorNotFound): Error404 {
        return Error404(error.message)
    }
}
