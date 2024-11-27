package perso.api.crypto.model

import java.math.BigDecimal

data class ResultDto(
    val token: String,
    val profit: BigDecimal,
    val profitPercent: String,
    val totalInvesti: BigDecimal,
    val currentValue: BigDecimal
) {
}