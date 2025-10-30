package perso.api.crypto.model

import perso.api.crypto.repository.database.model.TokenMetadata

data class TokenMetadataDto (
    val tokenId: String,
    val symbol: String,
    val name: String,
) {
    companion object {
        fun build(tokenMetadata: TokenMetadata): TokenMetadataDto {
            return TokenMetadataDto(
                tokenId = tokenMetadata.tokenId,
                symbol = tokenMetadata.symbol,
                name = tokenMetadata.name
            )
        }
    }
}