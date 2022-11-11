package oganesyan.rsoi_lab2.gateway.model

data class GatewayBookInfo(
    val bookUid: String? = null,
    val name: String? = null,
    val author: String? = null,
    val genre: String? = null,
    val condition: String? = null,
    val availableCount: Long? = null,
    var response: GatewayBaseResponse = GatewayBaseResponse()
)