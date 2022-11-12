package oganesyan.rsoi_lab2.gateway

class Const {
    companion object {
        // TODO ЕСЛИ GATEWAY запускается через IntellijIdea, то нужно все хосты поменять на HOST(localhost), если же в
        //  Докере, то на соответствующие

        const val HOST = "http://localhost"

        private const val HOST_GATEWAY = "http://gateway"
        private const val HOST_LIBRARY = "http://library"
        private const val HOST_RATING = "http://rating"
        private const val HOST_RESERVATION = "http://reservation"

//        private const val HOST_GATEWAY = HOST
//        private const val HOST_LIBRARY = HOST
//        private const val HOST_RATING = HOST
//        private const val HOST_RESERVATION = HOST

        const val LIBRARY = "library"
        const val RATING = "rating"
        const val RESERVATION = "reservation"

        // Ссылки на проверку жизнеспособности сервисов
        const val URL_HEALTH_gateway = "$HOST_GATEWAY:8060/gateway-system/manage/health"
        const val URL_HEALTH_library = "$HOST_LIBRARY:8060/library-system/manage/health"
        const val URL_HEALTH_rating = "$HOST_RATING:8050/rating-system/manage/health"
        const val URL_HEALTH_reservation = "$HOST_RESERVATION:8070/reservation-system/manage/health"

        // Ссылки на сервисы
        const val URL_Library_getLibraryByCity = "$HOST_LIBRARY:8060/library-system/getLibraryByCity"
        const val URL_Library_getAvailableCountByBookUidAndLibraryUid = "$HOST_LIBRARY:8060/library-system/library-books/getAvailableCountByBookUidAndLibraryUid"
        const val URL_Library_changeAvailableCountByBookUidAndLibraryUid = "$HOST_LIBRARY:8060/library-system/library-books/changeAvailableCountByBookUidAndLibraryUid"
        const val URL_Library_getBooksByLibrary = "$HOST_LIBRARY:8060/library-system/books/getBooksByLibrary"
        const val URL_Library_getBookByUid = "$HOST_LIBRARY:8060/library-system/books/getBookByUid"
        const val URL_Library_getLibraryByUid = "$HOST_LIBRARY:8060/library-system/getLibraryByUid"

        const val URL_Rating_setRatingByUsername = "$HOST_RATING:8050/rating-system/setRatingByUsername"
        const val URL_Rating_getRatingByUsername = "$HOST_RATING:8050/rating-system/getRatingByUsername"

        const val URL_Reservation_removeReservation = "$HOST_RESERVATION:8070/reservation-system/removeReservation"
        const val URL_Reservation_putReservation = "$HOST_RESERVATION:8070/reservation-system/putReservation"
        const val URL_Reservation_getReservationsByUsername = "$HOST_RESERVATION:8070/reservation-system/getReservationsByUsername"
    }
}