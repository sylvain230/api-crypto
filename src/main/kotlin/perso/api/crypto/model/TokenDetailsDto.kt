package perso.api.crypto.model

import perso.api.crypto.repository.http.model.coingecko.CoinDetailsJson
import perso.api.crypto.repository.http.model.coingecko.TokenJson

data class TokenDetailsDto(
    val name: String,
    val rank: Int? = null,
    val athPrice: Double? = null,
    val price: Double,
    val percent24h: Double
) {
    companion object {
        fun build(coinDetailsJson: CoinDetailsJson): TokenDetailsDto {
            return TokenDetailsDto(
                name = coinDetailsJson.id,
                rank = coinDetailsJson.market_cap_rank,
                athPrice = coinDetailsJson.market_data.ath["usd"]!!,
                price = coinDetailsJson.market_data.current_price["usd"]!!,
                percent24h = coinDetailsJson.market_data.price_change_percentage_24h
            )
        }

        fun buildSimple(maptokensJson: Map<String,TokenJson>): List<TokenDetailsDto> {
            return maptokensJson.entries.map { currentToken ->
                TokenDetailsDto(
                    name = currentToken.key,
                    price = currentToken.value.usd,
                    percent24h = currentToken.value.usd_24h_change
                )
            }.toList()
        }
    }
}