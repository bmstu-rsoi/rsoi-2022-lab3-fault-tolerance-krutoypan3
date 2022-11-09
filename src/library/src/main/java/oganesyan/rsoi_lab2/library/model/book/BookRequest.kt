package oganesyan.rsoi_lab2.library.model.book

data class BookRequest(
    val library_uid: String?,
    val page: Int?,
    val size: Int?,
    val showAll: Boolean?
)
