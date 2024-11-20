package perso.api.crypto.model

import perso.api.crypto.repository.http.paprika.model.InfosCoinJson

data class CoinDto(
    val id: String
) {
    companion object {
        fun build(infosCoinJson: InfosCoinJson): CoinDto {
           return CoinDto(infosCoinJson.id)
        }
    }
}


