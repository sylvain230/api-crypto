package perso.api.crypto.model

import perso.api.crypto.repository.database.model.Transaction
import java.time.LocalDateTime

data class TransactionDto(
    val token: String,
    val amount: Double,
    val dateTime: LocalDateTime
) {
    companion object {
        fun build(transaction: Transaction): TransactionDto {
            return TransactionDto(
                token = transaction.tokenId,
                amount = transaction.amount,
                dateTime =  transaction.datetime
            )
        }
    }
}