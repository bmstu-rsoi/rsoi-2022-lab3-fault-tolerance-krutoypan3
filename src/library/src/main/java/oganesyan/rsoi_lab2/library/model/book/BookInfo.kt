package oganesyan.rsoi_lab2.library.model.book

data class BookInfo(
    val bookUid: String?,
    val name: String?,
    val author: String?,
    val genre: String?,
    val condition: String?,
    val avaiblableCount: Long?
)