package oganesyan.rsoi_lab2.reservation.model

data class CreateReservationResponse(
    var status: String?,
    var startDate: String?,
    var tillDate: String?,
    var reservation_uid: String?,
)