package perso.api.crypto.repository.database

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import perso.api.crypto.repository.database.model.Transaction

@Repository
interface TransactionRepository : CrudRepository<Transaction, Int> {

    @Query("""
        select tr from Transaction tr where tr.tokenId = :tokenId
    """)
    fun findByTokenId(@Param("tokenId") tokenId: String): List<Transaction>

}