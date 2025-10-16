package perso.api.crypto.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.model.MarketChartWithLocalDate
import perso.api.crypto.model.ProfitDto
import perso.api.crypto.model.ResultDto
import perso.api.crypto.model.TokenDetailsDto
import perso.api.crypto.repository.database.TransactionRepository
import perso.api.crypto.repository.http.CoinGeckoRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class TokenService(
    private val coinGeckoRepository: CoinGeckoRepository,
    private val transactionRepository: TransactionRepository,
    private val historicalDataService: HistoricalDataService,
    private val mapper: ObjectMapper
) {

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

    fun saveTransaction(transaction: TransactionJson) {
//        if(transaction.date != null) {
//            val dataHistorical = findPriceByTokenAndDate(transaction.token, transaction.date!!)
//            transactionRepository.save(Transaction(
//                tokenId = transaction.token,
//                price = dataHistorical.price.setScale(2, RoundingMode.HALF_UP),
//                amount = transaction.amount,
//                datetime = LocalDate.parse(transaction.date!!),
//                )
//            )
//        } else {
//            val coin = findPricesTokenById(transaction.token)
//            transactionRepository.save(Transaction(
//                tokenId = transaction.token,
//                price = coin.price.setScale(2, RoundingMode.HALF_UP),
//                amount = transaction.amount,
//                datetime = LocalDate.now(),
//                )
//            )
//        }
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