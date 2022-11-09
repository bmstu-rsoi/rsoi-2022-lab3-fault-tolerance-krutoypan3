package oganesyan.rsoi_lab2.library.model.library

data class LibraryRequest(
    val page: Int?,
    val size: Int?,
    val city: String?
)