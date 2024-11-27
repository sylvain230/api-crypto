package perso.api.crypto.service

import org.springframework.stereotype.Service
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.model.*
import perso.api.crypto.repository.database.TransactionRepository
import perso.api.crypto.repository.database.model.Transaction
import perso.api.crypto.repository.http.PaprikaRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class TokenService(
    private val paprikaRepository: PaprikaRepository,
    private val transactionRepository: TransactionRepository
) {

    fun findInformationTokenById(id: String): CoinDto {
        return CoinDto.build(infosCoinJson = paprikaRepository.getInformationTokenById(id)!!)
    }

    fun findPricesTokenById(id: String) : TokenDto {
        return TokenDto.build(tokenJson = paprikaRepository.getPriceTokensById(id)!!)
    }

    fun saveTransaction(transaction: TransactionJson) {
        if(transaction.date != null) {
            val dataHistorical = findPriceByTokenAndDate(transaction.token, transaction.date!!)
            transactionRepository.save(Transaction(
                tokenId = transaction.token,
                price = dataHistorical.price.setScale(2, RoundingMode.HALF_UP),
                amount = transaction.amount,
                datetime = LocalDate.parse(transaction.date!!),
                )
            )
        } else {
            val coin = findPricesTokenById(transaction.token)
            transactionRepository.save(Transaction(
                tokenId = transaction.token,
                price = coin.price.setScale(2),
                amount = transaction.amount,
                datetime = LocalDate.now(),
                )
            )
        }
    }

    fun calculateProfitByToken(id: String): ResultDto {
        val coin = findPricesTokenById(id)
        var profit = BigDecimal.ZERO
        var totalInvest = BigDecimal.ZERO
        var totalAmountToday = BigDecimal.ZERO
        transactionRepository.findByTokenId(id).map {
            val amountWhenBuying = it.amount.multiply(it.price)
            val amountToday = it.amount.multiply(coin.price)
            totalAmountToday += amountToday
            profit += (amountToday - amountWhenBuying)
            totalInvest += amountWhenBuying
        }
        return ResultDto (
            token = id,
            profit = profit,
            profitPercent = profit.divide(totalInvest, 2, RoundingMode.HALF_UP).multiply(BigDecimal(100)).toString(),
            totalInvesti = totalInvest,
            currentValue = totalAmountToday
        )
    }

    fun findPriceByTokenAndDate(id: String, date: String): DataHistoricalDto {
        return DataHistoricalDto.build(dataHistoricalJson = paprikaRepository.findPriceByTokenAndDate(id, date)!!)
    }

    fun calculateProfit(): List<ResultDto> {
        return transactionRepository.findTokens().map { calculateProfitByToken(it) }
    }

    fun calculateProfitTotal(): ProfitDto {
        val profitsByCoin = calculateProfit()
        val totalInvesti = profitsByCoin.sumOf { it.totalInvesti }
        val totalValueWallet = profitsByCoin.sumOf { it.currentValue }

        return ProfitDto(
            totalInvesti = totalInvesti,
            totalValueWallet = totalValueWallet,
            percent = "${totalInvesti.divide(totalValueWallet, 2 ,RoundingMode.HALF_UP).multiply((BigDecimal(100)))} %"
        )
    }
}