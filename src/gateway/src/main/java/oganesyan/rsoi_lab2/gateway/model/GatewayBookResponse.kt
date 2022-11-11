package oganesyan.rsoi_lab2.gateway.model

data class GatewayBookResponse(
    val page: Int? = null,
    val pageSize: Int? = null,
    val totalElements: Int? = null,
    val items: List<GatewayBookInfo>? = null,
    var response: GatewayBaseResponse = GatewayBaseResponse()
)
