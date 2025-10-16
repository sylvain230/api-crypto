package perso.api.crypto.repository.http.model.coingecko

import perso.api.crypto.model.DailyData
import perso.api.crypto.model.MarketChartWithLocalDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class MarketChartJson(
    val prices: List<List<Double>>,
    val market_caps: List<List<Double>>,
    val total_volumes: List<List<Double>>
) {
    fun timestampToLocalDate(marketChart: MarketChartJson?): MarketChartWithLocalDate {
        return MarketChartWithLocalDate(
            prices = marketChart?.prices!!.map { data -> DailyData(
                date =  data[0].toLong().toLocalDate(ZoneId.of("UTC")),
                price = data[1])
            },
            marketCaps = marketChart.market_caps.map { data -> DailyData(
                date =  data[0].toLong().toLocalDate(ZoneId.of("UTC")),
                price = data[1])
            },
            totalVolumes = marketChart.total_volumes.map { data -> DailyData(
                date =  data[0].toLong().toLocalDate(ZoneId.of("UTC")),
                price = data[1])
            }
        )
    }

    private fun Long.toLocalDate(zoneId: ZoneId = ZoneId.of("UTC")): LocalDate {
        return Instant.ofEpochMilli(this)
            .atZone(zoneId)
            .toLocalDate()
    }
}