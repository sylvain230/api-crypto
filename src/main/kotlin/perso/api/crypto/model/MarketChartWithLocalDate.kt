package perso.api.crypto.model

import java.math.BigDecimal
import java.time.LocalDate

data class MarketChartWithLocalDate(
    val prices: List<DailyData>,
    val marketCaps: List<DailyData>,
    val totalVolumes: List<DailyData>)

data class DailyData(
    val date: LocalDate,
    val price: BigDecimal
)
