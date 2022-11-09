package oganesyan.rsoi_lab2.library.service

import oganesyan.rsoi_lab2.library.model.book.BookInfo
import oganesyan.rsoi_lab2.library.model.book.BookRequest
import oganesyan.rsoi_lab2.library.model.book.BookResponse
import oganesyan.rsoi_lab2.library.model.book.CreateBookRequest
import oganesyan.rsoi_lab2.library.model.library_book.BookIdUidResponse
import org.springframework.transaction.annotation.Transactional

interface BookService {
    @Transactional(readOnly = true)
    fun getBooksByLibrary(bookRequest: BookRequest): BookResponse

    @Transactional(readOnly = true)
    fun getBookIdByUid(book_uid: String?): BookIdUidResponse

    fun getBookByUid(book_uid: String?): BookInfo

    fun putBooks(createBookRequest: CreateBookRequest)
}