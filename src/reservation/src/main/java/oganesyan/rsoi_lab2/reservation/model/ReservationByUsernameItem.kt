package oganesyan.rsoi_lab2.reservation.model

data class ReservationByUsernameItem(
    var reservation_uid: String? = null,
    var username: String? = null,
    var book_uid: String? = null,
    var library_uid: String? = null,
    var status: String? = null,
    var start_date: String? = null,
    var till_date: String? = null,
)
