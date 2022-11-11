package oganesyan.rsoi_lab2.gateway.model

data class GatewayReservationResponse(
    var status: String? = null,
    var startDate: String? = null,
    var tillDate: String? = null,
    var reservationUid: String? = null,

    var book: GatewayBookInfo? = null,
    var library: GatewayLibraryInfo? = null,

    var response: GatewayBaseResponse = GatewayBaseResponse()
)