package oganesyan.rsoi_lab2.gateway.service

import oganesyan.rsoi_lab2.gateway.error.ErrorBadRequest
import oganesyan.rsoi_lab2.gateway.error.ErrorNotFound
import oganesyan.rsoi_lab2.gateway.model.*
import org.json.JSONObject
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder
import java.text.SimpleDateFormat
import java.util.*

@Transactional
@Service
class GatewayLibraryServiceImpl: GatewayLibraryService {


    override fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse {
        val url = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/getLibraryByCity")
            .queryParam("page", libraryRequest.page)
            .queryParam("size", libraryRequest.size)
            .queryParam("city", libraryRequest.city)
            .toUriString()

        val obj = getObjByUrl(url)

        val totalElements = obj.getInt("totalElements")
        val librariesInfoJsonArray = obj.getJSONArray("items")

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
        val url = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/books/getBooksByLibrary")
            .queryParam("library_uid", gatewayBooksByLibraryRequest.library_uid)
            .queryParam("page", gatewayBooksByLibraryRequest.page)
            .queryParam("size", gatewayBooksByLibraryRequest.size)
            .queryParam("showAll", gatewayBooksByLibraryRequest.showAll)
            .toUriString()

        val obj = getObjByUrl(url)

        val totalElements = obj.getInt("totalElements")

        val booksInfoJsonArray = obj.getJSONArray("items")

        val count: Int = booksInfoJsonArray.length()
        val booksInfo: ArrayList<GatewayBookInfo> = ArrayList(count)
        for (i in 0 until count) {
            val jsonBook: JSONObject = booksInfoJsonArray.getJSONObject(i)
            val bookInfo: GatewayBookInfo = parseGatewayBookInfo(jsonBook, gatewayBooksByLibraryRequest.library_uid?: "")
            booksInfo.add(bookInfo)
        }
        return GatewayBookResponse(gatewayBooksByLibraryRequest.page, gatewayBooksByLibraryRequest.size, totalElements, booksInfo)
    }

    override fun getRating(username: String): GatewayRatingResponse {
        val url = UriComponentsBuilder.fromHttpUrl("http://rating:8050/rating-system/getRatingByUsername")
            .queryParam("username", username)
            .toUriString()

        val obj = getObjByUrl(url)

        return GatewayRatingResponse(username, obj.getInt("stars"))
    }

    override fun setReservation(
        username: String,
        gatewayReservationRequest: GatewayReservationRequest,
    ): GatewayReservationResponse {

        val url = UriComponentsBuilder.fromHttpUrl("http://reservation:8070/reservation-system/putReservation")
            .queryParam("username", username)
            .queryParam("bookUid", gatewayReservationRequest.bookUid)
            .queryParam("libraryUid", gatewayReservationRequest.libraryUid)
            .queryParam("tillDate", gatewayReservationRequest.tillDate)
            .toUriString()

        val obj = getObjByUrl(url)

        println("\n$obj\n")

        val urlBook = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/books/getBookByUid")
            .queryParam("book_uid", gatewayReservationRequest.bookUid)
            .toUriString()
        val objBook = getObjByUrl(urlBook)

        val url2 = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/library-books/getAvailableCountByBookUidAndLibraryUid")
            .queryParam("library_uid", gatewayReservationRequest.libraryUid)
            .queryParam("book_uid", gatewayReservationRequest.bookUid)
            .toUriString()
        val obj2 = getObjByUrl(url2)

        println("\n$obj2\n")

        val bookInfo = GatewayBookInfo(
            objBook.getString("bookUid"),
            objBook.getString("name"),
            objBook.getString("author"),
            objBook.getString("genre"),
            objBook.getString("condition"),
            obj2.getLong("available_count"), // TODO тут ТАК-ТО NULL ПРИХОДИТ, НУЖНО САМОМУ ПОЛУЧАТЬ ЭТО ЧИСЛО \\ Upd. Сделал вроде бы
        )

        val urlLibrary = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/getLibraryByUid")
            .queryParam("library_uid", gatewayReservationRequest.libraryUid)
            .toUriString()
        val objLibrary = getObjByUrl(urlLibrary)
        val libraryInfo = GatewayLibraryInfo(
            libraryUid = objLibrary.getString("libraryUid"),
            name = objLibrary.getString("name"),
            city = objLibrary.getString("city"),
            address = objLibrary.getString("address"),
        )

        return GatewayReservationResponse(
            obj.getString("status"),
            obj.getString("startDate"),
            obj.getString("tillDate"),
            obj.getString("reservation_uid"),
            bookInfo,
            libraryInfo
        )
    }

    override fun getReservation(username: String): ArrayList<GatewayReservationResponse> {
        val url = UriComponentsBuilder.fromHttpUrl("http://reservation:8070/reservation-system/getReservationsByUsername")
            .queryParam("username", username)
            .toUriString()

        val obj = getObjByUrl(url)

        println("\n$obj\n")

        val obj2 = obj.getJSONArray("reservations")

        println("\n$obj2\n")

        val items: ArrayList<GatewayReservationResponse> = arrayListOf()

        val size = obj2.length()
        for (i in 0 until size){
            val obj22 = obj2.getJSONObject(i)

            val bookUid = obj22.getString("book_uid")
            val libraryUid = obj22.getString("library_uid")

            val urlBook = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/books/getBookByUid")
                .queryParam("book_uid", bookUid)
                .toUriString()
            val objBook = getObjByUrl(urlBook)

            val url2 = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/library-books/getAvailableCountByBookUidAndLibraryUid")
                .queryParam("library_uid", libraryUid)
                .queryParam("book_uid", bookUid)
                .toUriString()
            val obj3 = getObjByUrl(url2)

            println("\n$obj3\n")

            val bookInfo = GatewayBookInfo(
                objBook.getString("bookUid"),
                objBook.getString("name"),
                objBook.getString("author"),
                objBook.getString("genre"),
                objBook.getString("condition"),
                obj3.getLong("available_count"), // TODO тут ТАК-ТО NULL ПРИХОДИТ, НУЖНО САМОМУ ПОЛУЧАТЬ ЭТО ЧИСЛО \\ Upd. Сделал вроде бы
            )

            val urlLibrary = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/getLibraryByUid")
                .queryParam("library_uid", libraryUid)
                .toUriString()
            val objLibrary = getObjByUrl(urlLibrary)
            val libraryInfo = GatewayLibraryInfo(
                libraryUid = objLibrary.getString("libraryUid"),
                name = objLibrary.getString("name"),
                city = objLibrary.getString("city"),
                address = objLibrary.getString("address"),
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

    override fun returnReservation(username: String, gatewayReservationReturnRequest: GatewayReservationReturnRequest, reservationUid: String) {
        // Тут мы меняем status кniggi на RETURNED или EXPIRED
        val url = UriComponentsBuilder.fromHttpUrl("http://reservation:8070/reservation-system/removeReservation")
            .queryParam("username", username)
            .queryParam("reservationUid", reservationUid)
            .queryParam("date", gatewayReservationReturnRequest.date)
            .toUriString()
        val objReservationByUsernameItem = getObjByUrl(url)

        // Тут мы меняем кол-во книг в библиотеке
        val url2 = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/library-books/changeAvailableCountByBookUidAndLibraryUid")
            .queryParam("book_uid", objReservationByUsernameItem.getString("book_uid"))
            .queryParam("library_uid", objReservationByUsernameItem.getString("library_uid"))
            .queryParam("available_count", 1)
            .toUriString()
        getObjByUrlNotResponse(url2)
        // TODO Нужно обновить кол-во книг в библиотеке + изменить статус книги \\ Upd. Сверху сделано.


        var stars = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val currentDate = Date().time

        val tillDate = sdf.parse(objReservationByUsernameItem.getString("till_date")).time

        if (currentDate > tillDate)
            stars -= 10 // Если просрочка, то отнимаем 10 звезд
        else
            stars += 1 // Если книга возвращена вовремя, то добавляем 1 звезду

        println("\n$username $stars\n")

        val url3 = UriComponentsBuilder.fromHttpUrl("http://rating:8050/rating-system/setRatingByUsername")
            .queryParam("username", username)
            .queryParam("stars", stars)
            .toUriString()

        getObjByUrlNotResponse(url3)
        // TODO Также нужно поднять \ опустить рейтинг пользователю \\ Upd. Сделано выше.
    }

    private fun parseGatewayBookInfo(obj: JSONObject, libraryUid: String): GatewayBookInfo {
        val bookUid = obj.getString("bookUid")
        val name = obj.getString("name")
        val author = obj.getString("author")
        val genre = obj.getString("genre")
        val condition = obj.getString("condition")

        val url = UriComponentsBuilder.fromHttpUrl("http://library:8060/library-system/library-books/getAvailableCountByBookUidAndLibraryUid")
            .queryParam("library_uid", libraryUid)
            .queryParam("book_uid", bookUid)
            .toUriString()
        val obj2 = getObjByUrl(url)
        println("\n$obj2\n")
        val availableCount = obj2.getString("available_count")
        return GatewayBookInfo(bookUid, name, author, genre, condition, availableCount.toLong())
    }

    private fun parseGatewayLibraryInfo(obj: JSONObject): GatewayLibraryInfo {
        val libraryUid = obj.getString("libraryUid")
        val name = obj.getString("name")
        val address = obj.getString("address")
        val city = obj.getString("city")
        return GatewayLibraryInfo(libraryUid, name, address, city)
    }

    private fun postObjByUrl(url: String): JSONObject{
        println("\nTESTO : POINT-1\n")
        val headers = HttpHeaders()
        headers[HttpHeaders.ACCEPT] = MediaType.APPLICATION_JSON_VALUE
        val entity: HttpEntity<*> = HttpEntity<Any>(headers)
        println("\nTESTO : POINT-2\n")
        val restOperations: RestOperations = RestTemplate()

        println("\nTESTO : $url\n")

        println("\nTESTO : POINT-3\n")
        val response: ResponseEntity<String> = try {
            restOperations.exchange(
                url,
                HttpMethod.POST,
                entity,
                String::class.java
            )
        } catch (e: HttpClientErrorException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        } catch (e: HttpServerErrorException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        } catch (e: RestClientException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        }
        if (response.statusCode == HttpStatus.NOT_FOUND) {
            throw ErrorNotFound(response.body?: "")
        }
        if (response.statusCode == HttpStatus.BAD_REQUEST) {
            throw ErrorBadRequest(response.body ?: "", ArrayList())
        }
        println("\nTESTO : POINT-4\n")
        return JSONObject(response.body)
    }

    private fun getObjByUrl(url: String): JSONObject{
        val headers = HttpHeaders()
        headers[HttpHeaders.ACCEPT] = MediaType.APPLICATION_JSON_VALUE
        val entity: HttpEntity<*> = HttpEntity<Any>(headers)

        val restOperations: RestOperations = RestTemplate()
        val response: ResponseEntity<String> = try {
            restOperations.exchange(
                url,
                HttpMethod.GET,
                entity,
                String::class.java
            )
        } catch (e: HttpClientErrorException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        } catch (e: HttpServerErrorException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        } catch (e: RestClientException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        }
        if (response.statusCode == HttpStatus.NOT_FOUND) {
            throw ErrorNotFound(response.body?: "")
        }
        if (response.statusCode == HttpStatus.BAD_REQUEST) {
            throw ErrorBadRequest(response.body ?: "", ArrayList())
        }
        return JSONObject(response.body)
    }

    private fun getObjByUrlNotResponse(url: String){
        val headers = HttpHeaders()
        headers[HttpHeaders.ACCEPT] = MediaType.APPLICATION_JSON_VALUE
        val entity: HttpEntity<*> = HttpEntity<Any>(headers)

        val restOperations: RestOperations = RestTemplate()
        try {
            restOperations.exchange(
                url,
                HttpMethod.GET,
                entity,
                String::class.java
            )
        } catch (e: HttpClientErrorException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        } catch (e: HttpServerErrorException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        } catch (e: RestClientException) {
            println(e)
            throw ErrorBadRequest(e.toString(), ArrayList())
        }
    }
}