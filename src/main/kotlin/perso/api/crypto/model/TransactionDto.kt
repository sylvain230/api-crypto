package perso.api.crypto.model

import perso.api.crypto.repository.database.model.Transaction
import java.time.Instant

data class TransactionDto(
    val id: Long,
    val token: String,
    val amount: Double,
    val dateTime: Instant
) {
    companion object {
        fun build(transaction: Transaction): TransactionDto {
            return TransactionDto(
                id = transaction.id,
                token = transaction.tokenMetadata.symbol,
                amount = transaction.amount,
                dateTime =  transaction.datetime
            )
        }
    }
}