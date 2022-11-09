package oganesyan.rsoi_lab2.gateway.model

data class GatewayBooksByLibraryRequest(
    val library_uid: String?,
    val page: Int?,
    val size: Int?,
    val showAll: Boolean?
)