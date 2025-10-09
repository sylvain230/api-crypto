package perso.api.crypto.controller.model

data class CryptoAssetJson(
    val id: String,
    val name: String,
    val amount: Double,
    val usdValue: Double,
    val percentageOfPortfolio: Double,
    val trend24h: Double
) {
}