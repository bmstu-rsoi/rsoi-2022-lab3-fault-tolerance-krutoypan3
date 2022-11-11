package oganesyan.rsoi_lab2.reservation.model

data class CreateReservationRequest(
    var username: String,
    var bookUid: String,
    var libraryUid: String,
    var tillDate: String,
    var stars: Int? = null,
    var available_count: Int? = null,
)