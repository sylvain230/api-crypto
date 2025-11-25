package perso.api.crypto.controller.model

import com.fasterxml.jackson.annotation.JsonInclude
import perso.api.crypto.model.TransactionDto
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TransactionJson(
    val id: Long,
    val token: String,
    val amount: String,
    val dateTransaction: Instant
) {
    companion object {

        fun build(transactionDto: TransactionDto): TransactionJson {
            return TransactionJson(
                id = transactionDto.id,
                token = transactionDto.token,
                amount = transactionDto.amount.toString(),
                dateTransaction = transactionDto.dateTime
            )
        }
    }
}