package oganesyan.rsoi_lab2.reservation.model

data class RemoveReservationRequest(
    var username: String,
    var reservationUid: String,
    var date: String,
)
