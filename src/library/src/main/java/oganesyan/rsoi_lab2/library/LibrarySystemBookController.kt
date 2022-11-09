package oganesyan.rsoi_lab2.library

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.library.model.*
import oganesyan.rsoi_lab2.library.model.book.BookRequest
import oganesyan.rsoi_lab2.library.model.book.BookResponse
import oganesyan.rsoi_lab2.library.model.book.CreateBookRequest
import oganesyan.rsoi_lab2.library.model.library_book.BookIdUidResponse
import oganesyan.rsoi_lab2.library.model.library_book.LibraryIdUidResponse
import oganesyan.rsoi_lab2.library.service.BookService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@Tag(name = "library_system_books_controller")
@RestController
@RequestMapping("/library-system/books")
class LibrarySystemBookController(private val bookService: BookService) {

    @Operation(
        summary = "get_book_by_library",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Books by Library",
                content = [Content(schema = Schema(implementation = BookResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found books for library"
            ),
        ]
    )
    @GetMapping("/getBooksByLibrary", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBooksByLibrary(
        @RequestParam library_uid: String?,
        @RequestParam page: Int?,
        @RequestParam size: Int?,
        @RequestParam showAll: Boolean?,
    ) = bookService.getBooksByLibrary(BookRequest(library_uid, page, size, showAll))


    @GetMapping("/getBookByUid", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBookByUid(
        @RequestParam("book_uid") book_uid: String?
    ) = bookService.getBookByUid(book_uid)


    @Operation(
        summary = "get_bookID_by_UID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "BookID by UID",
                content = [Content(schema = Schema(implementation = BookIdUidResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found book"
            ),
        ]
    )
    @GetMapping("/getBookIDByUID", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBookIDByUID(@RequestParam("book_uid") book_uid: String?) = bookService.getBookIdByUid(book_uid)

    @Operation(
        summary = "create_book", responses = [
            ApiResponse(
                responseCode = "201",
                description = "Created new book",
                headers = [Header(name = "Location", description = "Path to new Book")]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid data"
            ),
            ApiResponse(
                responseCode = "501", description = "Сервер так смешно делает пых-пых"
            )
        ]
    )
    @PostMapping("/putBook", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createBook(@Valid @RequestBody request: CreateBookRequest): ResponseEntity<Void> {
        bookService.putBooks(request)
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).build()
    }
}