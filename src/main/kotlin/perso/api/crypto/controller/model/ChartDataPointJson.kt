package perso.api.crypto.controller.model

import perso.api.crypto.model.ChartDataPointDto

data class ChartDataPointJson(
    val date: String,
    val value: Double
) {
    companion object {
        fun build(chartDataPointDto: ChartDataPointDto): ChartDataPointJson {
            return ChartDataPointJson(
                chartDataPointDto.date,
                chartDataPointDto.value
            )
        }

        fun buildList(chartDataPointDtos: List<ChartDataPointDto>): List<ChartDataPointJson> {
            return chartDataPointDtos.map { build(it) }
        }
    }
}