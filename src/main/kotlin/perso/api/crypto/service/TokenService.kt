package perso.api.crypto.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.model.*
import perso.api.crypto.repository.database.TokenMetadataRepository
import perso.api.crypto.repository.database.TransactionRepository
import perso.api.crypto.repository.http.CoinGeckoRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class TokenService(
    private val coinGeckoRepository: CoinGeckoRepository,
    private val transactionRepository: TransactionRepository,
    private val historicalDataService: HistoricalDataService,
    private val tokenMetadataRepository: TokenMetadataRepository,
    private val mapper: ObjectMapper
) {

    fun getAllCoins(): List<TokenMetadataDto> {
        return tokenMetadataRepository.findAll().map { TokenMetadataDto.build(it) }
    }

    fun getPriceByTokenAndDate(id: String, date: LocalDate): Double {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = date.format(formatter)
        return coinGeckoRepository.getPriceByTokenAndDate(id, formattedDate)?.market_data?.current_price?.get("usd")
            ?: throw CryptoException("Prix USD non trouvé pour le token $id au $formattedDate.")
    }

    fun getDetailsInformationTokenById(id: String): TokenDetailsDto {
        return TokenDetailsDto.build(coinDetailsJson = coinGeckoRepository.getInformationTokenById(id)!!)
    }

    fun getSimpleInformationsTokenByIds(ids: String): List<TokenDetailsDto> {
        return TokenDetailsDto.buildSimple(maptokensJson = coinGeckoRepository.getPriceTokensById(ids)!!)
    }

    fun findPriceByTokenAndDate(tokenId: String, date: LocalDate): Double? {
        val historical = historicalDataService.findDataById(tokenId)?.historical ?: return null
        val marketChart = mapper.readValue(historical, MarketChartWithLocalDate::class.java)
        return marketChart.prices.find { it.date == date }?.price
    }

    fun findYearlyHistoricalPrices(tokenIds: List<String>): Map<String,Map<LocalDate, Double>> {
        val mapTokenPrices :MutableMap<String,Map<LocalDate, Double>> = mutableMapOf()
        tokenIds.forEach { token ->
            val historical = historicalDataService.findDataById(token)?.historical
            val marketChart = mapper.readValue(historical, MarketChartWithLocalDate::class.java)
            val mapDatePrice: MutableMap<LocalDate, Double> = mutableMapOf()
            mapTokenPrices[token] = mapDatePrice
            marketChart.prices.sortedBy { it.date }.forEach { currentPrice ->
                mapDatePrice[currentPrice.date] = currentPrice.price
            }
        }
        return mapTokenPrices
    }

    fun calculateProfitByToken(id: String): ResultDto {
        val coin = getDetailsInformationTokenById(id)
        var profit = BigDecimal.ZERO
        var totalInvest = BigDecimal.ZERO
        var totalAmountToday = BigDecimal.ZERO
        val transactions = transactionRepository.findByTokenId(id)

        if(transactions.isEmpty()) {
            throw CryptoException("Pas de transaction pour le token $id.")
        }

//        transactions.map {
//            val amountWhenBuying = it.amount.multiply(it.price)
//            val amountToday = it.amount.multiply(coin.price)
//            val amountWhenBuying = it.amount.multiply(it.price)
//            val amountToday = it.amount.multiply(coin.price)
//            totalAmountToday += amountToday
//            profit += (amountToday - amountWhenBuying)
//            totalInvest += amountWhenBuying
//        }

        val profitPercent = profit.divide(totalInvest, 2, RoundingMode.HALF_UP).multiply(BigDecimal(100))

        return ResultDto (
            token = id,
            profit = profit.setScale(2, RoundingMode.HALF_UP),
            profitPercent =
                when(profitPercent.signum()) {
                    -1 -> " - $profitPercent %"
                    1 -> " + $profitPercent %"
                    else -> " + $profitPercent %"
                },
            totalInvesti = totalInvest.setScale(2, RoundingMode.HALF_UP),
            currentValue = totalAmountToday.setScale(2, RoundingMode.HALF_UP)
        )
    }

//    fun findPriceByTokenAndDate(id: String, date: String): DataHistoricalDto {
//        return DataHistoricalDto.build(dataHistoricalJson = paprikaRepository.findPriceByTokenAndDate(id, date)!!)
//    }

    fun calculateProfit(): List<ResultDto> {
        return transactionRepository.findTokens().map { calculateProfitByToken(it) }
    }

    fun calculateProfitTotal(): ProfitDto {
        val profitsByCoin = calculateProfit()
        val totalInvesti = profitsByCoin.sumOf { it.totalInvesti }
        val totalValueWallet = profitsByCoin.sumOf { it.currentValue }

        return ProfitDto(
            totalInvesti = totalInvesti.setScale(2, RoundingMode.HALF_UP),
            totalValueWallet = totalValueWallet.setScale(2, RoundingMode.HALF_UP),
            percent = "${totalInvesti.divide(totalValueWallet, 2 ,RoundingMode.HALF_UP).multiply((BigDecimal(100)))} %"
        )
    }

}