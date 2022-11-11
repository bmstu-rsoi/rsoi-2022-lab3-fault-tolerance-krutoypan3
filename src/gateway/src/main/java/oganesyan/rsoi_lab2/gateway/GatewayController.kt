package oganesyan.rsoi_lab2.gateway

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.gateway.model.*
import oganesyan.rsoi_lab2.gateway.service.GatewayLibraryService
import org.json.JSONObject
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.*
import java.util.*

@Tag(name = "library_system_controller")
@RestController
@RequestMapping("/api/v1")
class GatewayController(private val gatewayLibraryService: GatewayLibraryService) {

    @GetMapping("/manage/health", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun manageHealth() = ResponseEntity<Void>(HttpStatus.OK)

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
    ): Any{
        val returnResponse = gatewayLibraryService.getLibraryByCity(GatewayLibraryRequest(city = city, page = page, size = size))
        if (returnResponse.response.error != null) return ResponseEntity(returnResponse.response.errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
        return returnResponse
    }

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
    ): Any{
        val returnResponse = gatewayLibraryService.getBooksByLibrary(
            GatewayBooksByLibraryRequest(
                library_uid = libraryUid,
                page = page,
                size = size,
                showAll = showAll
            )
        )
        if (returnResponse.response.error != null) return ResponseEntity(returnResponse.response.errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
        return returnResponse
    }

    @GetMapping("/rating", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRating(
        @RequestHeader(value = "X-User-Name") username: String,
    ): Any{
        val returnResponse = gatewayLibraryService.getRating(username)
        if (returnResponse.response.error != null) return ResponseEntity(returnResponse.response.errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
        return returnResponse
    }

    @PostMapping("/reservations", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun setReservation(
        @RequestHeader(value = "X-User-Name") username: String,
        @RequestBody gatewayReservationRequest: GatewayReservationRequest,
    ): Any{
        val returnResponse = gatewayLibraryService.setReservation(username, gatewayReservationRequest)
        if (returnResponse.response.error != null) return ResponseEntity(returnResponse.response.errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
        return returnResponse
    }


    @PostMapping("/reservations/{reservationUid}/return", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun returnReservation(
        @RequestHeader(value = "X-User-Name") username: String,
        @RequestBody gatewayReservationReturnRequest: GatewayReservationReturnRequest,
        @PathVariable reservationUid: String,
    ): Any{
        val response = gatewayLibraryService.returnReservation(username, gatewayReservationReturnRequest, reservationUid)
        if (response.error != null) return ResponseEntity(response.errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
        return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/reservations", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getReservation(
        @RequestHeader(value = "X-User-Name") username: String,
    ): Any{
        val returnResponse = gatewayLibraryService.getReservation(username)
        if (returnResponse[0].response.error != null) return ResponseEntity(returnResponse[0].response.errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
        return returnResponse
    }
}