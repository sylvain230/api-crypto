package perso.api.crypto.controller.model

import java.math.BigDecimal

data class TransactionJson(
    val token: String = "",
    val amount: BigDecimal = BigDecimal.ZERO
) {
}