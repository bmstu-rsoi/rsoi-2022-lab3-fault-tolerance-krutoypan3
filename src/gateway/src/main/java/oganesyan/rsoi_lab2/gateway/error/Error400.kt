package oganesyan.rsoi_lab2.gateway.error

class Error400 internal constructor(error: ErrorBadRequest) {
    var message: String
    var errors: List<Error400Description> = ArrayList()

    init {
        message = error.message
        errors = error.errors
    }
}