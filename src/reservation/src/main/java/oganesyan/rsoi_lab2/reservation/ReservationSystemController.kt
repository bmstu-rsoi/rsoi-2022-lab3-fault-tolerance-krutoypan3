package oganesyan.rsoi_lab2.reservation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.reservation.model.CreateReservationRequest
import oganesyan.rsoi_lab2.reservation.model.CreateReservationResponse
import oganesyan.rsoi_lab2.reservation.model.RemoveReservationRequest
import oganesyan.rsoi_lab2.reservation.model.ReservationByUsernameItemResponse
import oganesyan.rsoi_lab2.reservation.service.ReservationService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid


@Tag(name = "library_system_controller")
@RestController
@RequestMapping("/reservation-system")
class ReservationSystemController(private val reservationService: ReservationService) {

    @Operation(
        summary = "get_reservations_by_username",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "reservations by username",
                content = [Content(schema = Schema(implementation = ReservationByUsernameItemResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found reservations for username"
            ),
        ]
    )
    @GetMapping("/getReservationsByUsername", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getReservationsByUsername(@RequestParam("username") username: String) =
        reservationService.getReservationsByUsername(username)

    @GetMapping("/putReservation", produces = [MediaType.APPLICATION_JSON_VALUE])
    private fun putReservation(
        @RequestParam("username") username: String,
        @RequestParam("bookUid") bookUid: String,
        @RequestParam("libraryUid") libraryUid: String,
        @RequestParam("tillDate") tillDate: String,
    ): CreateReservationResponse = reservationService.putReservation(
        CreateReservationRequest(username, bookUid, libraryUid, tillDate)
    )

    @GetMapping("/removeReservation", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun removeReservation(
        @RequestParam("username") username: String,
        @RequestParam("reservationUid") reservationUid: String,
        @RequestParam("date") date: String,
    ) = reservationService.removeReservation(
        RemoveReservationRequest(
            username = username,
            reservationUid = reservationUid,
            date = date
        )
    )
}