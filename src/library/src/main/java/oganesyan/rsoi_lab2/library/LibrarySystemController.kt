package oganesyan.rsoi_lab2.library

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.library.model.*
import oganesyan.rsoi_lab2.library.model.library.CreateLibraryRequest
import oganesyan.rsoi_lab2.library.model.library.LibraryRequest
import oganesyan.rsoi_lab2.library.model.library.LibraryResponse
import oganesyan.rsoi_lab2.library.model.library_book.LibraryIdUidResponse
import oganesyan.rsoi_lab2.library.service.LibraryService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import javax.validation.Valid

@Tag(name = "library_system_controller")
@RestController
@RequestMapping("/library-system")
class LibrarySystemController(private val libraryService: LibraryService) {

    @GetMapping("/manage/health", produces = [APPLICATION_JSON_VALUE])
    fun manageHealth() = ResponseEntity<Void>(HttpStatus.OK)

    @Operation(
        summary = "get_library_by_city",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Library by city",
                content = [Content(schema = Schema(implementation = LibraryResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found library for city"
            ),
        ]
    )
    @GetMapping("/getLibraryByCity", produces = [APPLICATION_JSON_VALUE])
    fun getLibraryByCity(
        @RequestParam("city") city: String?, @RequestParam("page") page: Int?, @RequestParam("size") size: Int?,
    ) = libraryService.getLibraryByCity(LibraryRequest(page, size, city))


    @Operation(
        summary = "get_libraryID_by_UID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "LibraryID by UID",
                content = [Content(schema = Schema(implementation = LibraryIdUidResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found library for city"
            ),
        ]
    )
    @GetMapping("/getLibraryIDByUID", produces = [APPLICATION_JSON_VALUE])
    fun getLibraryIDByUID(@RequestParam("library_uid") library_uid: String?) = libraryService.getLibraryIdByUid(library_uid)

    @GetMapping("/getLibraryByUid", produces = [APPLICATION_JSON_VALUE])
    fun getLibraryByUid(@RequestParam("library_uid") library_uid: String?) = libraryService.getLibraryByUid(library_uid)

    @Operation(
        summary = "get_libraries",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "All Library",
                content = [Content(schema = Schema(implementation = LibraryResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found libraries", content = [Content(schema = Schema())]
            ),
        ]
    )
    @GetMapping("/getAllLibrary", produces = [APPLICATION_JSON_VALUE])
    fun getAllLibrary(@RequestParam("page") page: Int?, @RequestParam("size") size: Int?) =
        libraryService.getAllLibrary(page, size)


    @Operation(
        summary = "create_library", responses = [
            ApiResponse(
                responseCode = "201",
                description = "Created new library",
                headers = [Header(name = "Location", description = "Path to new Library")]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid data"
            ),
            ApiResponse(
                responseCode = "501", description = "Сервер так смешно делает пых-пых"
            )
        ]
    )
    @PostMapping("/putLibrary", consumes = [APPLICATION_JSON_VALUE])
    fun createLibrary(@Valid @RequestBody request: CreateLibraryRequest): ResponseEntity<Void> {
        libraryService.putLibrary(request)
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).build()
    }
}
