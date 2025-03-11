package perso.api.crypto.model

import perso.api.crypto.repository.http.paprika.model.TokenJson
import java.math.BigDecimal

data class TokenDto(
    val name: String,
    val rank: Int,
    val athPrice: BigDecimal,
    val price: BigDecimal
) {
    companion object {
        fun build(tokenJson: TokenJson): TokenDto {
            return TokenDto(
                name = tokenJson.name,
                rank = Integer.valueOf(tokenJson.rank),
                athPrice = tokenJson.quotes.USD.ath_price,
                price = tokenJson.quotes.USD.price)
        }
    }
}