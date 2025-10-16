package perso.api.crypto.repository.database

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import perso.api.crypto.repository.database.model.HistoricalData

@Repository
interface HistoricalDataRepository: CrudRepository<HistoricalData, Int> {

    @Query("""
        select h from HistoricalData h where h.tokenId = :tokenId
    """)
    fun findByTokenId(@Param("tokenId") tokenId: String): HistoricalData?
}