package oganesyan.rsoi_lab2.gateway.model

data class GatewayLibraryResponse(
    val page: Int?,
    val pageSize: Int?,
    val totalElements: Int?,
    val items: List<GatewayLibraryInfo>,
)