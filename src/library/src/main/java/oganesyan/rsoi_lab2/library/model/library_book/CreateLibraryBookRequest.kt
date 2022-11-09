package oganesyan.rsoi_lab2.library.model.library_book

data class CreateLibraryBookRequest(
    var book_id: Int?,
    var library_id: Int?,
    var available_count: Int?,
)