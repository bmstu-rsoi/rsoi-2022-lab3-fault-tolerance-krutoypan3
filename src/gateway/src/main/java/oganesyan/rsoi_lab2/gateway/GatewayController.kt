package oganesyan.rsoi_lab2.gateway

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.gateway.model.*
import oganesyan.rsoi_lab2.gateway.service.GatewayLibraryService
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

@Tag(name = "library_system_controller")
@RestController
@RequestMapping("/api/v1")
class GatewayController(private val gatewayLibraryService: GatewayLibraryService) {

    @Operation(
        summary = "get_library_by_city",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Library by city",
                content = [Content(schema = Schema(implementation = GatewayLibraryResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found library for city"
            ),
        ]
    )
    @GetMapping("/libraries", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLibraryByCity(
        @RequestParam("city") city: String?, @RequestParam("page") page: Int?, @RequestParam("size") size: Int?,
    ) = gatewayLibraryService.getLibraryByCity(GatewayLibraryRequest(city = city, page = page, size = size))

    @Operation(
        summary = "get_books_by_library",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Books by library",
                content = [Content(schema = Schema(implementation = GatewayBookResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found book for library"
            ),
        ]
    )
    @GetMapping("/libraries/{libraryUid}/books", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBooksByLibrary(
        @PathVariable("libraryUid") libraryUid: String,
        @RequestParam("page") page: Int?,
        @RequestParam("size") size: Int?,
        @RequestParam("showAll") showAll: Boolean?,
    ) = gatewayLibraryService.getBooksByLibrary(
        GatewayBooksByLibraryRequest(
            library_uid = libraryUid,
            page = page,
            size = size,
            showAll = showAll
        )
    )

    @GetMapping("/rating")
    fun getRating(
        @RequestHeader(value = "X-User-Name") username: String,
    ) = gatewayLibraryService.getRating(username)

    @PostMapping("/reservations")
    fun setReservation(
        @RequestHeader(value = "X-User-Name") username: String,
        @RequestBody gatewayReservationRequest: GatewayReservationRequest,
    ) = gatewayLibraryService.setReservation(username, gatewayReservationRequest)


    @PostMapping("/reservations/{reservationUid}/return")
    fun returnReservation(
        @RequestHeader(value = "X-User-Name") username: String,
        @RequestBody gatewayReservationReturnRequest: GatewayReservationReturnRequest,
        @PathVariable reservationUid: String,
    ): ResponseEntity<Void> {
        gatewayLibraryService.returnReservation(username, gatewayReservationReturnRequest, reservationUid)
        return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/reservations")
    fun getReservation(
        @RequestHeader(value = "X-User-Name") username: String,
    ) = gatewayLibraryService.getReservation(username)
}