package perso.api.crypto.model

import java.math.BigDecimal

data class CryptoAssetDto(
    val id: String,
    val name: String,
    val amount: BigDecimal,
    val usdValue: BigDecimal,
    val percentageOfPortfolio: BigDecimal,
    val trend24h: BigDecimal
)