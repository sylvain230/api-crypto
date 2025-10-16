package perso.api.crypto.repository.http.model.coingecko

data class CoinDetailsJson(
    val id: String,
    val symbol: String,
    val market_cap_rank: Int,
    val market_data : CurrentPrice,
)

class CurrentPrice(
    val current_price: HashMap<String, Double>,
    val ath: HashMap<String, Double>,
    val price_change_percentage_24h: Double
)
