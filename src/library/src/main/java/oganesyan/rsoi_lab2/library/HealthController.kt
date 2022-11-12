package oganesyan.rsoi_lab2.library

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "library_system_controller_health")
@RestController
@RequestMapping("")
class HealthController {
    @GetMapping("/manage/health", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun manageHealth() = ResponseEntity<Void>(HttpStatus.OK)
}