package perso.api.crypto.repository.http.model.coingecko

import perso.api.crypto.model.DailyData
import perso.api.crypto.model.MarketChartWithLocalDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class MarketChartJson(
    val prices: List<List<String>>,
    val market_caps: List<List<String>>,
    val total_volumes: List<List<String>>
) {
    fun timestampToLocalDate(marketChart: MarketChartJson?): MarketChartWithLocalDate {
        return MarketChartWithLocalDate(
            prices = marketChart?.prices!!.map { data -> DailyData(
                date =  data[0].toLong().toLocalDate(ZoneId.of("UTC")),
                price = data[1].toBigDecimal())
            },
            marketCaps = marketChart.market_caps.map { data -> DailyData(
                date =  data[0].toLong().toLocalDate(ZoneId.of("UTC")),
                price = data[1].toBigDecimal())
            },
            totalVolumes = marketChart.total_volumes.map { data -> DailyData(
                date =  data[0].toLong().toLocalDate(ZoneId.of("UTC")),
                price = data[1].toBigDecimal())
            }
        )
    }

    private fun Long.toLocalDate(zoneId: ZoneId = ZoneId.of("UTC")): LocalDate {
        return Instant.ofEpochMilli(this)
            .atZone(zoneId)
            .toLocalDate()
    }
}