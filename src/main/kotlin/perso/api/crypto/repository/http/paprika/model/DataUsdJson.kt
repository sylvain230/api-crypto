package perso.api.crypto.repository.http.paprika.model

import java.math.BigDecimal

data class DataUsdJson(
    val price: BigDecimal,
    val ath_price: BigDecimal,
    val percent_change_24h: BigDecimal
)
