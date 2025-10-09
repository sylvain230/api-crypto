package perso.api.crypto.repository.http.paprika.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class TokenJson(
    val id: String,
    val name: String,
    val rank: String,
    val max_supply: Double,
    val quotes: QuotesJson
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QuotesJson(
    @JsonProperty("USD")
    val USD: DataUsdJson
)

data class DataUsdJson(
    val price: Double,
    val ath_price: BigDecimal,
    val percent_change_24h: Double
)