package perso.api.crypto.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDto(
    val token: String,
    val amount: BigDecimal,
    val dateTime: LocalDateTime
)