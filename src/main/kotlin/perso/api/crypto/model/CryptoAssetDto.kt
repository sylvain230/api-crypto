package perso.api.crypto.model

data class CryptoAssetDto(
    val id: String,
    val name: String,
    val amount: Double,
    val usdValue: Double,
    val percentageOfPortfolio: Double,
    val trend24h: Double
)