package perso.api.crypto.model

import java.math.BigDecimal

data class ChartDataPointDto(
    val date: String,
    val value: BigDecimal
)