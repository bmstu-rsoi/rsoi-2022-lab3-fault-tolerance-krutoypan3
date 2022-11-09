package oganesyan.rsoi_lab2.library.model.book

data class BookResponse(
    val page: Int?,
    val pageSize: Int?,
    val totalElements: Int?,
    val items: List<BookInfo>
)