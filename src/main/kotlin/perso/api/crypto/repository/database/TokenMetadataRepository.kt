package perso.api.crypto.repository.database

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import perso.api.crypto.repository.database.model.TokenMetadata

@Repository
interface TokenMetadataRepository: CrudRepository<TokenMetadata, String> {

    fun findBySymbol(symbol: String): TokenMetadata
}