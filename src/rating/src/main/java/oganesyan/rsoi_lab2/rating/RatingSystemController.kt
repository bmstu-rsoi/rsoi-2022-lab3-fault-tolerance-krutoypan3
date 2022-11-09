package oganesyan.rsoi_lab2.rating

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import oganesyan.rsoi_lab2.rating.model.RatingResponse
import oganesyan.rsoi_lab2.rating.model.SetRatingRequest
import oganesyan.rsoi_lab2.rating.service.RatingService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@Tag(name = "rating_system_controller")
@RestController
@RequestMapping("/rating-system")
class RatingSystemController(private val ratingService: RatingService) {

    @GetMapping("/manage/health", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun manageHealth() = ResponseEntity<Void>(HttpStatus.OK)

    @Operation(
        summary = "get_rating_by_username",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Rating by Username",
                content = [Content(schema = Schema(implementation = RatingResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found rating for username"
            ),
        ]
    )
    @GetMapping("/getRatingByUsername", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRatingByUsername(@RequestParam("username") username: String)
    = ratingService.getRatingByUsername(username = username)


    @Operation(
        summary = "update_rating_by_username", responses = [
            ApiResponse(
                responseCode = "201",
                description = "Rating updated",
                headers = [Header(name = "Location", description = "Update rating")]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid data"
            ),
            ApiResponse(
                responseCode = "501", description = "Сервер так смешно делает пых-пых"
            )
        ]
    )
    @GetMapping("/setRatingByUsername", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createLibrary(
        @RequestParam("username") username: String,
        @RequestParam("stars") stars: Int
    ) = ratingService.setRatingByUsername(SetRatingRequest(username = username, stars = stars))
}