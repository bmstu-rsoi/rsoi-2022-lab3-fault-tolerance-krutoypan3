package oganesyan.rsoi_lab2.gateway.model

data class GatewayBookInfo(
    val bookUid: String?,
    val name: String?,
    val author: String?,
    val genre: String?,
    val condition: String?,
    val availableCount: Long?
)