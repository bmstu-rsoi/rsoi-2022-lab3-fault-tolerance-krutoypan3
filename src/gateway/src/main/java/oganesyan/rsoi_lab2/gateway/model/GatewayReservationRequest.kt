package oganesyan.rsoi_lab2.gateway.model

data class GatewayReservationRequest(
    var bookUid: String,
    var libraryUid: String,
    var tillDate: String
)