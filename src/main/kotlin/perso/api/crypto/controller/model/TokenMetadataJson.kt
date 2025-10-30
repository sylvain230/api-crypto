package perso.api.crypto.controller.model

import perso.api.crypto.model.TokenMetadataDto

data class TokenMetadataJson (
    val tokenId: String,
    val symbol: String,
    val name: String,
) {
    companion object {
        fun build(tokenMetadata: TokenMetadataDto): TokenMetadataJson {
            return TokenMetadataJson(
                tokenId = tokenMetadata.tokenId,
                symbol = tokenMetadata.symbol,
                name = tokenMetadata.name
            )
        }
    }
}