package oganesyan.rsoi_lab2.library.model.library

data class LibraryResponse(
    val page: Int?,
    val pageSize: Int?,
    val totalElements: Int?,
    val items: List<LibraryInfo>
)