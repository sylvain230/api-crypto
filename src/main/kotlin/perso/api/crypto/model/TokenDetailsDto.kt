package perso.api.crypto.model

import perso.api.crypto.repository.http.model.coingecko.CoinDetailsJson
import perso.api.crypto.repository.http.model.coingecko.TokenJson
import java.math.BigDecimal

data class TokenDetailsDto(
    val name: String,
    val rank: Int? = null,
    val athPrice: BigDecimal? = null,
    val price: BigDecimal,
    val percent24h: BigDecimal
) {
    companion object {
        fun build(coinDetailsJson: CoinDetailsJson): TokenDetailsDto {
            return TokenDetailsDto(
                name = coinDetailsJson.id,
                rank = coinDetailsJson.market_cap_rank,
                athPrice = coinDetailsJson.market_data.ath["usd"]!!.toBigDecimal(),
                price = coinDetailsJson.market_data.current_price["usd"]!!.toBigDecimal(),
                percent24h = coinDetailsJson.market_data.price_change_percentage_24h.toBigDecimal()
            )
        }

        fun buildSimple(maptokensJson: Map<String,TokenJson>): List<TokenDetailsDto> {
            return maptokensJson.entries.map { currentToken ->
                TokenDetailsDto(
                    name = currentToken.key,
                    price = currentToken.value.usd.toBigDecimal(),
                    percent24h = currentToken.value.usd_24h_change.toBigDecimal()
                )
            }.toList()
        }
    }
}