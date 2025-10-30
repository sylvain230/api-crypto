package perso.api.crypto.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.stereotype.Service
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.repository.database.HistoricalDataRepository
import perso.api.crypto.repository.database.TokenMetadataRepository
import perso.api.crypto.repository.database.model.HistoricalData
import perso.api.crypto.repository.http.CoinGeckoRepository
import java.time.LocalDateTime

// Définissez la période après laquelle le cache est considéré comme périmé (ex: 1 jour)
private const val CACHE_EXPIRY_HOURS = 24L

@Service
class HistoricalDataService(
    private val historicalDataRepository: HistoricalDataRepository,
    private val coinGeckoRepository: CoinGeckoRepository,
    private val tokenMetadataRepository: TokenMetadataRepository,
    private val gson: Gson
) {

    fun findDataById(tokenId: String): HistoricalData? {
        val historicalData = historicalDataRepository.findByTokenId(tokenId)

        val isCacheExpired = historicalData == null ||
                historicalData.lastUpdated.plusHours(CACHE_EXPIRY_HOURS)
                    .isBefore(LocalDateTime.now())

       return if(isCacheExpired) {
            val marketChart = coinGeckoRepository.getMarketChart(id = tokenId)
            val marketChatWithLocalDate = marketChart?.timestampToLocalDate(marketChart)
            if(historicalData == null) {
                historicalDataRepository.save(HistoricalData(
                    tokenMetadata = tokenMetadataRepository.findById(tokenId).orElseThrow
                        { CryptoException("Token $tokenId non trouvé en base.") },
                    historical = gson.toJson(marketChatWithLocalDate)
                ))
            } else {
                historicalData.historical = gson.toJson(marketChatWithLocalDate)
                historicalData.lastUpdated = LocalDateTime.now()
                historicalDataRepository.save(historicalData)
            }
        } else {
            historicalData
        }
    }
}