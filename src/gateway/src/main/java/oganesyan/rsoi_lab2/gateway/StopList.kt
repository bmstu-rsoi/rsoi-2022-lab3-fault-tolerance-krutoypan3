package oganesyan.rsoi_lab2.gateway

import oganesyan.rsoi_lab2.gateway.GatewayApp.logger
import oganesyan.rsoi_lab2.gateway.Queries.getObjByUrl
import org.springframework.http.HttpStatus

class StopList : Thread() {
    private val unSentRequests = ArrayDeque<String>()
    private val waitTime = 5000L

    fun add(url: String) {
        logger.warn("URL был добавлен в стоп-лист: $url")
        unSentRequests.addLast(url)
    }

    override fun run() {
        super.run()

        val iterator = unSentRequests.iterator()
        while (true) {
            while (iterator.hasNext()) {
                val currentUrl = iterator.next()
                val changeRatingResult = getObjByUrl(currentUrl)
                logger.info("Осуществляем попытку отправить запрос по указанному URL: $currentUrl")
                if (changeRatingResult.response.statusCode != HttpStatus.NOT_FOUND) {
                    unSentRequests.remove(currentUrl)
                    logger.info("URL был выполнен и удален из стоп-листа: $currentUrl")
                } else logger.info("Не удалось отправить запрос по указанному URL, повторная попытка через ${waitTime / 1000} секунд: $currentUrl")
            }
            sleep(waitTime)
        }
    }
}