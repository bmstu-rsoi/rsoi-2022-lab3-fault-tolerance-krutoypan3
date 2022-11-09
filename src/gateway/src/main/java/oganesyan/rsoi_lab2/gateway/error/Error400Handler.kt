package oganesyan.rsoi_lab2.gateway.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class Error400Handler {
    @ExceptionHandler(ErrorBadRequest::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestError(error: ErrorBadRequest): Error400 {
        return Error400(error)
    }
}