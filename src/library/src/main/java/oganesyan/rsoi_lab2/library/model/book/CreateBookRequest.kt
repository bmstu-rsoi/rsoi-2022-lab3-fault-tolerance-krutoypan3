package oganesyan.rsoi_lab2.library.model.book

data class CreateBookRequest(
    val name: String?,
    val author: String?,
    val genre: String?,
    val condition: String?
)