package oganesyan.rsoi_lab2.gateway.model

import org.springframework.http.HttpStatus
import org.springframework.web.client.RestClientException

class GatewayBaseResponse(
    var statusCode: HttpStatus = HttpStatus.OK,
    var error: RestClientException? = null,
    var errorMessage: String? = null
)