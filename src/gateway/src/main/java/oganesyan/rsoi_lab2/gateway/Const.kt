package oganesyan.rsoi_lab2.gateway

class Const {
    companion object {
        const val HOST = "http://localhost"

        const val LIBRARY = "library"
        const val RATING = "rating"
        const val RESERVATION = "reservation"

        // Ссылки на проверку жизнеспособности сервисов
        const val URL_HEALTH_gateway = "$HOST:8060/gateway-system/manage/health"
        const val URL_HEALTH_library = "$HOST:8060/library-system/manage/health"
        const val URL_HEALTH_rating = "$HOST:8050/rating-system/manage/health"
        const val URL_HEALTH_reservation = "$HOST:8070/reservation-system/manage/health"

        // Ссылки на сервисы
        const val URL_Library_getLibraryByCity = "$HOST:8060/library-system/getLibraryByCity"
        const val URL_Library_getAvailableCountByBookUidAndLibraryUid = "$HOST:8060/library-system/library-books/getAvailableCountByBookUidAndLibraryUid"
        const val URL_Library_changeAvailableCountByBookUidAndLibraryUid = "$HOST:8060/library-system/library-books/changeAvailableCountByBookUidAndLibraryUid"
        const val URL_Library_getBooksByLibrary = "$HOST:8060/library-system/books/getBooksByLibrary"
        const val URL_Library_getBookByUid = "$HOST:8060/library-system/books/getBookByUid"
        const val URL_Library_getLibraryByUid = "$HOST:8060/library-system/getLibraryByUid"

        const val URL_Rating_setRatingByUsername = "$HOST:8050/rating-system/setRatingByUsername"
        const val URL_Rating_getRatingByUsername = "$HOST:8050/rating-system/getRatingByUsername"

        const val URL_Reservation_removeReservation = "$HOST:8070/reservation-system/removeReservation"
        const val URL_Reservation_putReservation = "$HOST:8070/reservation-system/putReservation"
        const val URL_Reservation_getReservationsByUsername = "$HOST:8070/reservation-system/getReservationsByUsername"
    }
}