package perso.api.crypto.repository.http.paprika.model

import java.math.BigDecimal

class DataHistoricalJson(
    val timestamp: String,
    val price: BigDecimal,
    val volume_24h: Long,
    val market_cap: Long
)
