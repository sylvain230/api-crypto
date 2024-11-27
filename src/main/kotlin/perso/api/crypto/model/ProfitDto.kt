package perso.api.crypto.model

import java.math.BigDecimal

data class ProfitDto(
    val totalInvesti: BigDecimal,
    val percent: String,
    val totalValueWallet: BigDecimal
) {

}
