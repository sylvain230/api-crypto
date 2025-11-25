package perso.api.crypto.controller.model

import perso.api.crypto.model.CryptoAssetDto

data class CryptoAssetJson(
    val id: String,
    val name: String,
    val amount: String,
    val usdValue: String,
    val percentageOfPortfolio: String,
    val trend24h: String
) {
    companion object {
        fun build(cryptoAssetDto: CryptoAssetDto): CryptoAssetJson {
            return CryptoAssetJson(
                id = cryptoAssetDto.id,
                name = cryptoAssetDto.name,
                amount = cryptoAssetDto.amount.toString(),
                usdValue = cryptoAssetDto.usdValue.toString(),
                percentageOfPortfolio = cryptoAssetDto.percentageOfPortfolio.toString(),
                trend24h = cryptoAssetDto.trend24h.toString()
            )
        }

        fun buildList(cryptoAssetDto: List<CryptoAssetDto>): List<CryptoAssetJson> {
            return cryptoAssetDto.map { build(it) }
        }
    }
}