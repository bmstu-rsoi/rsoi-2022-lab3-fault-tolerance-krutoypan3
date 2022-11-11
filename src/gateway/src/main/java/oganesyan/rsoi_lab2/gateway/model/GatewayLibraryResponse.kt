package oganesyan.rsoi_lab2.gateway.model

data class GatewayLibraryResponse(
    val page: Int? = null,
    val pageSize: Int? = null,
    val totalElements: Int? = null,
    val items: List<GatewayLibraryInfo>? = null,
    var response: GatewayBaseResponse = GatewayBaseResponse()
)