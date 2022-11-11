package oganesyan.rsoi_lab2.gateway

import oganesyan.rsoi_lab2.gateway.model.GatewayUrlResponse
import org.json.JSONObject
import org.springframework.http.*
import org.springframework.web.client.*
import java.net.ConnectException

/**
 * Тут у нас ведется работа со ссылками (отправка запросов \ разбиение ссылки на составляющие)
 */
object Queries {

    /**
     * Получение GET запроса по URL
     * @param url Ссылка на запрашиваемый ресурс
     * @return Тело ответа и его техническая часть
     */
    fun getObjByUrl(url: String): GatewayUrlResponse {
        val headers = HttpHeaders()
        headers[HttpHeaders.ACCEPT] = MediaType.APPLICATION_JSON_VALUE
        val entity: HttpEntity<*> = HttpEntity<Any>(headers)
        val returnResponse = GatewayUrlResponse()

        val restOperations: RestOperations = RestTemplate()
        try {
            val response: ResponseEntity<String> = restOperations.exchange(
                url, HttpMethod.GET, entity, String::class.java
            )
            returnResponse.response.statusCode = response.statusCode
            returnResponse.jsonObject = JSONObject(response.body ?: "{}")
        } catch (e: HttpClientErrorException) {
            returnResponse.response.error = e
        } catch (e: HttpServerErrorException) {
            returnResponse.response.error = e
        } catch (e: RestClientException) {
            returnResponse.response.error = e
            returnResponse.response.errorMessage =
                "{\"message\": \"${getServiceByUrl(url = url)} Service unavailable\"}"
            returnResponse.response.statusCode = HttpStatus.NOT_FOUND
        }
        return returnResponse
    }

    /**
     * Получение название сервиса в ... А Я ВООБЩЕ ХЗ ОТКУДА ЭТО ВЗЯТО В ПОСТМАНЕ. НУ раз так в тз, то и как бы претензий нет
     */
    private fun getServiceByUrl(url: String): String {
        return when (url.split("-system")[0].split("/")[url.split("-system")[0].split("/").size - 1]) {
            Const.LIBRARY -> "Library"
            Const.RATING -> "Bonus" // В ДУШЕ НЕ ЧАЮ почему в Postman в тестах написан BONUS, а не RATING
            Const.RESERVATION -> "Reservation"
            else -> "Ducking"
        }
    }
}