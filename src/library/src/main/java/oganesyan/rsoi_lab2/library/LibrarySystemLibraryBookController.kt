package oganesyan.rsoi_lab2.library

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.library.model.library_book.CreateLibraryBookRequest
import oganesyan.rsoi_lab2.library.model.library_book.LibraryBookInfo
import oganesyan.rsoi_lab2.library.model.library_book.LibraryBookInfoResponse
import oganesyan.rsoi_lab2.library.service.LibraryBookService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@Tag(name = "library_system_books_controller")
@RestController
@RequestMapping("/library-system/library-books")
class LibrarySystemLibraryBookController(private val libraryBookService: LibraryBookService) {
    @Operation(
        summary = "get_booksID_by_libraryID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "BooksID by LibraryID",
                content = [Content(schema = Schema(implementation = LibraryBookInfoResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found books for library uid"
            ),
        ]
    )
    @GetMapping("/getBooksIdByLibraryId", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBooksIdByLibraryId(
        @RequestParam("library_id") library_id: Int?,
    ) = libraryBookService.getBooksIdByLibraryId(library_id)


    @Operation(
        summary = "create_library_book", responses = [
            ApiResponse(
                responseCode = "201",
                description = "Created new library book svyaz",
                headers = [Header(name = "Location", description = "Path to new Library book svyaaaaaaaaaaaaaaaz")]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid data? Сам ты инвалид! Вообще обнаглел, обзывается уже!"
            ),
            ApiResponse(
                responseCode = "501", description = "Сервер так смешно делает пых-пых"
            )
        ]
    )
    @PostMapping("/putLibraryBook", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createLibraryBook(@Valid @RequestBody request: CreateLibraryBookRequest): ResponseEntity<Void> {
        libraryBookService.putLibraryBook(request)
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).build()
    }

    @Operation(
        summary = "get_available_count_by_book_uid_and_library_uid",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Available count by BookUid and LibraryUid",
                content = [Content(schema = Schema(implementation = LibraryBookInfo::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found books for library uid"
            ),
        ]
    )
    @GetMapping("/getAvailableCountByBookUidAndLibraryUid", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAvailableCountByBookUidAndLibraryUid(
        @RequestParam("book_uid") book_uid: String?,
        @RequestParam("library_uid") library_uid: String?,
    ) = libraryBookService.getAvailableCountByBookUidAndLibraryUid(book_uid = book_uid, library_uid = library_uid)

    @GetMapping("/changeAvailableCountByBookUidAndLibraryUid", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun changeAvailableCountByBookUidAndLibraryUid(
        @RequestParam("book_uid") book_uid: String?,
        @RequestParam("library_uid") library_uid: String?,
        @RequestParam("available_count") available_count: Int?
    ) = libraryBookService.changeAvailableCountByBookUidAndLibraryUid(book_uid = book_uid, library_uid = library_uid, available_count = available_count)

}