package perso.api.crypto.repository.http.model.coingecko

data class CoinDetailsJson(
    val id: String,
    val symbol: String,
    val market_cap_rank: Int,
    val market_data : CurrentPrice,
)

class CurrentPrice(
    val current_price: HashMap<String, String>,
    val ath: HashMap<String, String>,
    val price_change_percentage_24h: String
)
