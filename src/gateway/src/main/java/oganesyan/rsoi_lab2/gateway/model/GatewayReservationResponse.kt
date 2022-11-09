package oganesyan.rsoi_lab2.gateway.model

data class GatewayReservationResponse(
    var status: String,
    var startDate: String,
    var tillDate: String,
    var reservationUid: String,

    var book: GatewayBookInfo,
    var library: GatewayLibraryInfo,
)