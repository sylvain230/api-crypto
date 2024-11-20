package perso.api.crypto.model

import perso.api.crypto.repository.http.paprika.model.DataHistoricalJson
import java.math.BigDecimal

data class DataHistoricalDto(
    val price: BigDecimal
) {
    companion object {
        fun build(dataHistoricalJson: DataHistoricalJson): DataHistoricalDto {
            return DataHistoricalDto(price = dataHistoricalJson.price)
        }
    }
}
