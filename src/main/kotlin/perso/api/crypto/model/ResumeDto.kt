package perso.api.crypto.model

import java.math.BigDecimal

data class ResumeDto(
    val totalInvest: BigDecimal,
    val amountToday: BigDecimal,
    val percentWinOrLoss: BigDecimal,
    val amountWinOrLoss: BigDecimal) {
}