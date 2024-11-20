package perso.api.crypto.model

import java.math.BigDecimal

data class ResultDto(
    val profit: BigDecimal,
    val profitPercent: String,
    val totalInvesti: BigDecimal
) {
}