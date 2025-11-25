package perso.api.crypto.repository.http.model.coingecko

data class HistoryJson(
    val id: String,
    val market_data: MarkeDataJson
)

data class MarkeDataJson(
    val current_price: Map<String, String>
)
