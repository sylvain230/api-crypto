package perso.api.crypto.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.model.*
import perso.api.crypto.repository.database.TokenMetadataRepository
import perso.api.crypto.repository.database.TransactionRepository
import perso.api.crypto.repository.http.CoinGeckoRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.ZoneId
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

    fun getPriceByTokenAndDate(id: String, date: LocalDate): BigDecimal {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = date.format(formatter)

        val priceString = coinGeckoRepository.getPriceByTokenAndDate(id, formattedDate)?.market_data?.current_price?.get("usd")
            ?: throw CryptoException("Prix USD non trouvé pour le token $id au $formattedDate.")

        return priceString.toBigDecimal()
    }

    fun getDetailsInformationTokenById(id: String): TokenDetailsDto {
        return TokenDetailsDto.build(coinDetailsJson = coinGeckoRepository.getInformationTokenById(id)!!)
    }

    fun getSimpleInformationsTokenByIds(ids: String): List<TokenDetailsDto> {
        return TokenDetailsDto.buildSimple(maptokensJson = coinGeckoRepository.getPriceTokensById(ids)!!)
    }

    fun findPriceByTokenAndDate(tokenId: String, date: LocalDate): BigDecimal? {
        val historical = historicalDataService.findDataById(tokenId)?.historical ?: return null
        val marketChart = mapper.readValue(historical, MarketChartWithLocalDate::class.java)
        return marketChart.prices.find { it.date == date }?.price
    }

    fun findYearlyHistoricalPrices(tokenIds: List<String>): Map<String,Map<LocalDate, BigDecimal>> {
        val mapTokenPrices :MutableMap<String,Map<LocalDate, BigDecimal>> = mutableMapOf()
        tokenIds.forEach { token ->
            val historical = historicalDataService.findDataById(token)?.historical
            val marketChart = mapper.readValue(historical, MarketChartWithLocalDate::class.java)
            val mapDatePrice: MutableMap<LocalDate, BigDecimal> = mutableMapOf()
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
        var totalAmountInvested = BigDecimal.ZERO
        var totalAmountToday = BigDecimal.ZERO
        val transactions = transactionRepository.findByTokenId(id)

        if(transactions.isEmpty()) {
            throw CryptoException("Pas de transaction pour le token $id.")
        }

        transactions.map {
            val priceWhenBuying = getPriceByTokenAndDate(it.tokenMetadata.tokenId, LocalDate.ofInstant(it.datetime, ZoneId.of("Europe/Paris")))
            val amountWhenBuying = it.amount.multiply(priceWhenBuying)
            val amountToday = it.amount.multiply(coin.price)
            totalAmountToday = totalAmountToday.add(amountToday)
            profit += (amountToday - amountWhenBuying)
            totalAmountInvested += amountWhenBuying
        }

        val profitPercent = profit.div(totalAmountInvested).multiply(BigDecimal(100.0))

        return ResultDto (
            token = id,
            profit = profit,
            profitPercent =
                when(profitPercent.signum()) {
                    -1 -> " - $profitPercent %"
                    1 -> " + $profitPercent %"
                    else -> " + $profitPercent %"
                },
            totalInvesti = totalAmountInvested.setScale(2, RoundingMode.HALF_UP),
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