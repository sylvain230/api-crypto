package perso.api.crypto.model

import java.time.LocalDate
import java.time.LocalDateTime

data class MarketChartWithLocalDate(
    val prices: List<DailyData>,
    val marketCaps: List<DailyData>,
    val totalVolumes: List<DailyData>)

data class DailyData(
    val date: LocalDate,
    val price: Double
)
