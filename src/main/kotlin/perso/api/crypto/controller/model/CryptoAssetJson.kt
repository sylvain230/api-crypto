package perso.api.crypto.controller.model

import perso.api.crypto.model.CryptoAssetDto

data class CryptoAssetJson(
    val id: String,
    val name: String,
    val amount: Double,
    val usdValue: Double,
    val percentageOfPortfolio: Double,
    val trend24h: Double
) {
    companion object {
        fun build(cryptoAssetDto: CryptoAssetDto): CryptoAssetJson {
            return CryptoAssetJson(
                id = cryptoAssetDto.id,
                name = cryptoAssetDto.name,
                amount = cryptoAssetDto.amount,
                usdValue = cryptoAssetDto.usdValue,
                percentageOfPortfolio = cryptoAssetDto.percentageOfPortfolio,
                trend24h = cryptoAssetDto.trend24h
            )
        }

        fun buildList(cryptoAssetDto: List<CryptoAssetDto>): List<CryptoAssetJson> {
            return cryptoAssetDto.map { build(it) }
        }
    }
}