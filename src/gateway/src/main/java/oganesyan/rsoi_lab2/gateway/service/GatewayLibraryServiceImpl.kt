package oganesyan.rsoi_lab2.gateway.service

import oganesyan.rsoi_lab2.gateway.Const
import oganesyan.rsoi_lab2.gateway.GatewayApp
import oganesyan.rsoi_lab2.gateway.Queries.getObjByUrl
import oganesyan.rsoi_lab2.gateway.model.*
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Transactional
@Service
class GatewayLibraryServiceImpl @Autowired constructor(restTemplateBuilder: RestTemplateBuilder): GatewayLibraryService {
    private val restTemplate = restTemplateBuilder.build()


    override fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse {
        getObjByUrl(UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_library).toUriString()).response.let { response ->
            response.error?.let {
                return GatewayLibraryResponse(
                    response = response
                )
            }
        }
        val url = UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getLibraryByCity)
            .queryParam("page", libraryRequest.page)
            .queryParam("size", libraryRequest.size)
            .queryParam("city", libraryRequest.city)
            .toUriString()

        val obj = getObjByUrl(url)

        val totalElements = obj.jsonObject!!.getInt("totalElements")
        val librariesInfoJsonArray = obj.jsonObject!!.getJSONArray("items")

        val count: Int = librariesInfoJsonArray.length()
        val librariesInfo: ArrayList<GatewayLibraryInfo> = ArrayList(count)
        for (i in 0 until count) {
            val jsonLibrary: JSONObject = librariesInfoJsonArray.getJSONObject(i)
            val libraryInfo: GatewayLibraryInfo = parseGatewayLibraryInfo(jsonLibrary)
            librariesInfo.add(libraryInfo)
        }
        return GatewayLibraryResponse(libraryRequest.page, libraryRequest.size, totalElements, librariesInfo)
    }

    override fun getBooksByLibrary(gatewayBooksByLibraryRequest: GatewayBooksByLibraryRequest): GatewayBookResponse {
        getObjByUrl(UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_library).toUriString()).response.let { response ->
            response.error?.let {
                return GatewayBookResponse(
                    response = response
                )
            }
        }

        val url = UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getBooksByLibrary)
            .queryParam("library_uid", gatewayBooksByLibraryRequest.library_uid)
            .queryParam("page", gatewayBooksByLibraryRequest.page)
            .queryParam("size", gatewayBooksByLibraryRequest.size)
            .queryParam("showAll", gatewayBooksByLibraryRequest.showAll)
            .toUriString()

        val obj = getObjByUrl(url)

        val totalElements = obj.jsonObject!!.getInt("totalElements")

        val booksInfoJsonArray = obj.jsonObject!!.getJSONArray("items")

        val count: Int = booksInfoJsonArray.length()
        val booksInfo: ArrayList<GatewayBookInfo> = ArrayList(count)
        for (i in 0 until count) {
            val jsonBook: JSONObject = booksInfoJsonArray.getJSONObject(i)
            val bookInfo: GatewayBookInfo =
                parseGatewayBookInfo(jsonBook, gatewayBooksByLibraryRequest.library_uid ?: "")
            booksInfo.add(bookInfo)
        }
        return GatewayBookResponse(
            gatewayBooksByLibraryRequest.page,
            gatewayBooksByLibraryRequest.size,
            totalElements,
            booksInfo
        )
    }

    override fun getRating(username: String): GatewayRatingResponse {
        getObjByUrl(UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_rating).toUriString()).response.let { response ->
            response.error?.let {
                return GatewayRatingResponse(
                    response = response
                )
            }
        }

        val url = UriComponentsBuilder.fromHttpUrl(Const.URL_Rating_getRatingByUsername)
            .queryParam("username", username)
            .toUriString()

        val obj = getObjByUrl(url)

        return GatewayRatingResponse(username, obj.jsonObject!!.getInt("stars"))
    }

    override fun setReservation(
        username: String,
        gatewayReservationRequest: GatewayReservationRequest,
    ): GatewayReservationResponse {

        getObjByUrl(
            UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_reservation).toUriString()
        ).response.let { response ->
            response.error?.let {
                return GatewayReservationResponse(
                    response = response
                )
            }
        }
        getObjByUrl(UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_library).toUriString()).response.let { response ->
            response.error?.let {
                return GatewayReservationResponse(
                    response = response
                )
            }
        }
        getObjByUrl(UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_rating).toUriString()).response.let { response ->
            response.error?.let {
                return GatewayReservationResponse(
                    response = response
                )
            }
        }

        // Здесь отправляем запрос на получение рейтинга пользователя по username
        val urlStars = "${Const.URL_Rating_getRatingByUsername}?username=$username"
        val stars = restTemplate.getForObject(urlStars, GatewayRatingResponse::class.java)?.stars ?: 30

        val urlAvailableCount = "${Const.URL_Library_getAvailableCountByBookUidAndLibraryUid}?book_uid=${gatewayReservationRequest.bookUid}&library_uid=${gatewayReservationRequest.libraryUid}"
        val availableCount = restTemplate.getForObject(urlAvailableCount, GatewayLibraryBookInfo::class.java)?.available_count



        val url = UriComponentsBuilder.fromHttpUrl(Const.URL_Reservation_putReservation)
            .queryParam("username", username)
            .queryParam("bookUid", gatewayReservationRequest.bookUid)
            .queryParam("libraryUid", gatewayReservationRequest.libraryUid)
            .queryParam("tillDate", gatewayReservationRequest.tillDate)
            .queryParam("stars",stars)
            .queryParam("available_count",availableCount)
            .toUriString()

        val obj = getObjByUrl(url)

        val urlBook = UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getBookByUid)
            .queryParam("book_uid", gatewayReservationRequest.bookUid)
            .toUriString()
        val objBook = getObjByUrl(urlBook)

        val url2 =
            UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getAvailableCountByBookUidAndLibraryUid)
                .queryParam("library_uid", gatewayReservationRequest.libraryUid)
                .queryParam("book_uid", gatewayReservationRequest.bookUid)
                .toUriString()
        val obj2 = getObjByUrl(url2)

        val bookInfo = GatewayBookInfo(
            objBook.jsonObject!!.getString("bookUid"),
            objBook.jsonObject!!.getString("name"),
            objBook.jsonObject!!.getString("author"),
            objBook.jsonObject!!.getString("genre"),
            objBook.jsonObject!!.getString("condition"),
            obj2.jsonObject!!.getLong("available_count"),
        )

        val urlLibrary = UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getLibraryByUid)
            .queryParam("library_uid", gatewayReservationRequest.libraryUid)
            .toUriString()
        val objLibrary = getObjByUrl(urlLibrary)
        val libraryInfo = GatewayLibraryInfo(
            libraryUid = objLibrary.jsonObject!!.getString("libraryUid"),
            name = objLibrary.jsonObject!!.getString("name"),
            city = objLibrary.jsonObject!!.getString("city"),
            address = objLibrary.jsonObject!!.getString("address"),
        )

        return GatewayReservationResponse(
            obj.jsonObject?.getString("status"),
            obj.jsonObject?.getString("startDate"),
            obj.jsonObject?.getString("tillDate"),
            obj.jsonObject?.getString("reservation_uid"),
            bookInfo,
            libraryInfo
        )
    }

    override fun getReservation(username: String): ArrayList<GatewayReservationResponse> {
        getObjByUrl(
            UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_reservation).toUriString()
        ).response.let { response ->
            response.error?.let {
                return arrayListOf(
                    GatewayReservationResponse(
                        response = response
                    )
                )
            }
        }
        getObjByUrl(UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_library).toUriString()).response.let { response ->
            response.error?.let {
                return arrayListOf(
                    GatewayReservationResponse(
                        response = response
                    )
                )
            }
        }

        val url =
            UriComponentsBuilder.fromHttpUrl(Const.URL_Reservation_getReservationsByUsername)
                .queryParam("username", username)
                .toUriString()

        val obj = getObjByUrl(url)

        println("\n$obj\n")

        val obj2 = obj.jsonObject!!.getJSONArray("reservations")

        println("\n$obj2\n")

        val items: ArrayList<GatewayReservationResponse> = arrayListOf()

        val size = obj2.length()
        for (i in 0 until size) {
            val obj22 = obj2.getJSONObject(i)

            val bookUid = obj22.getString("book_uid")
            val libraryUid = obj22.getString("library_uid")

            val urlBook = UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getBookByUid)
                .queryParam("book_uid", bookUid)
                .toUriString()
            val objBook = getObjByUrl(urlBook)

            val url2 =
                UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getAvailableCountByBookUidAndLibraryUid)
                    .queryParam("library_uid", libraryUid)
                    .queryParam("book_uid", bookUid)
                    .toUriString()
            val obj3 = getObjByUrl(url2)

            println("\n$obj3\n")

            val bookInfo = GatewayBookInfo(
                objBook.jsonObject!!.getString("bookUid"),
                objBook.jsonObject!!.getString("name"),
                objBook.jsonObject!!.getString("author"),
                objBook.jsonObject!!.getString("genre"),
                objBook.jsonObject!!.getString("condition"),
                obj3.jsonObject!!.getLong("available_count"),
            )

            val urlLibrary = UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getLibraryByUid)
                .queryParam("library_uid", libraryUid)
                .toUriString()
            val objLibrary = getObjByUrl(urlLibrary)
            val libraryInfo = GatewayLibraryInfo(
                libraryUid = objLibrary.jsonObject!!.getString("libraryUid"),
                name = objLibrary.jsonObject!!.getString("name"),
                city = objLibrary.jsonObject!!.getString("city"),
                address = objLibrary.jsonObject!!.getString("address"),
            )

            val sdf = SimpleDateFormat("yyyy-MM-dd")

            items.add(
                GatewayReservationResponse(
                    status = obj22.getString("status"),
                    startDate = sdf.format(sdf.parse(obj22.getString("start_date"))), // TODO Сейчас тут формат |YYYY-MM-DD hh:mm:ss:xxx| \\ Upd. вроде исправил
                    tillDate = sdf.format(sdf.parse(obj22.getString("till_date"))),
                    reservationUid = obj22.getString("reservation_uid"),
                    book = bookInfo,
                    library = libraryInfo,
                )
            )
        }

        return items
    }

    override fun returnReservation(
        username: String,
        gatewayReservationReturnRequest: GatewayReservationReturnRequest,
        reservationUid: String,
    ): GatewayBaseResponse {

        getObjByUrl(
            UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_reservation).toUriString()
        ).response.let { response ->
            response.error?.let {
                return response
            }
        }
        getObjByUrl(UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_library).toUriString()).response.let { response ->
            response.error?.let {
                return response
            }
        }

        // Тут мы меняем status кniggi на RETURNED или EXPIRED
        val url = UriComponentsBuilder.fromHttpUrl(Const.URL_Reservation_removeReservation)
            .queryParam("username", username)
            .queryParam("reservationUid", reservationUid)
            .queryParam("date", gatewayReservationReturnRequest.date)
            .toUriString()
        val objReservationByUsernameItem = getObjByUrl(url)

        // Тут мы меняем кол-во книг в библиотеке
        val url2 = UriComponentsBuilder.fromHttpUrl(Const.URL_Library_changeAvailableCountByBookUidAndLibraryUid)
            .queryParam("book_uid", objReservationByUsernameItem.jsonObject!!.getString("book_uid"))
            .queryParam("library_uid", objReservationByUsernameItem.jsonObject!!.getString("library_uid"))
            .queryParam("available_count", 1)
            .toUriString()
        getObjByUrl(url2)


        var stars = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val currentDate = Date().time + 10800000 // Добавляем 3 часа (т.к. в Postman, в тестах используется Московское время)

        val tillDate = sdf.parse(objReservationByUsernameItem.jsonObject!!.getString("till_date")).time

        if (currentDate > tillDate)
            stars -= 10 // Если просрочка, то отнимаем 10 звезд
        else
            stars += 1 // Если книга возвращена вовремя, то добавляем 1 звезду

        val url3 = UriComponentsBuilder.fromHttpUrl(Const.URL_Rating_setRatingByUsername)
            .queryParam("username", username)
            .queryParam("stars", stars)
            .toUriString()

        val changeRatingResult = getObjByUrl(url3)

        if (changeRatingResult.response.statusCode == HttpStatus.NOT_FOUND)
            GatewayApp.stopList.add(url3)

        return GatewayBaseResponse()
    }

    private fun parseGatewayBookInfo(obj: JSONObject, libraryUid: String): GatewayBookInfo {
        val bookUid = obj.getString("bookUid")
        val name = obj.getString("name")
        val author = obj.getString("author")
        val genre = obj.getString("genre")
        val condition = obj.getString("condition")

        getObjByUrl(UriComponentsBuilder.fromHttpUrl(Const.URL_HEALTH_library).toUriString()).response.let { response ->
            response.error?.let {
                return GatewayBookInfo(
                    response = response
                )
            }
        }

        val url = UriComponentsBuilder.fromHttpUrl(Const.URL_Library_getAvailableCountByBookUidAndLibraryUid)
            .queryParam("library_uid", libraryUid)
            .queryParam("book_uid", bookUid)
            .toUriString()
        val jsonObject = getObjByUrl(url)
        println("\n$jsonObject\n")
        val availableCount = jsonObject.jsonObject!!.getString("available_count")
        return GatewayBookInfo(
            bookUid = bookUid,
            name = name,
            author = author,
            genre = genre,
            condition = condition,
            availableCount = availableCount.toLong()
        )
    }

    private fun parseGatewayLibraryInfo(obj: JSONObject): GatewayLibraryInfo {
        val libraryUid = obj.getString("libraryUid")
        val name = obj.getString("name")
        val address = obj.getString("address")
        val city = obj.getString("city")
        return GatewayLibraryInfo(libraryUid, name, address, city)
    }
}