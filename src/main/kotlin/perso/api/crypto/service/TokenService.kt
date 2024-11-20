package perso.api.crypto.service

import org.springframework.stereotype.Service
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.model.*
import perso.api.crypto.repository.database.TransactionRepository
import perso.api.crypto.repository.database.model.Transaction
import perso.api.crypto.repository.http.PaprikaRepository
import java.math.BigDecimal
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
        val coin = findPricesTokenById(transaction.token)
        transactionRepository.save(Transaction(
            tokenId = transaction.token,
            price = coin.price,
            amount = transaction.amount,
            datetime = LocalDate.now(),
        ))
    }

    fun calculateProfit(id: String): ResultDto {
        val coin = findPricesTokenById(id)
        var profit = BigDecimal.ZERO
        var totalInvesti = BigDecimal.ZERO
        transactionRepository.findByTokenId(id).map {
            val amountWhenBuying = it.amount * it.price
            val amountToday = it.amount * coin.price
            profit += (amountToday - amountWhenBuying)
            totalInvesti += amountWhenBuying
        }
        return ResultDto (
            profit = profit,
            profitPercent = profit.divide(totalInvesti).multiply(BigDecimal(100)).toString(),
            totalInvesti = totalInvesti
        )
    }

    fun findPriceByTokenAndDate(id: String, date: String): DataHistoricalDto {
        return DataHistoricalDto.build(dataHistoricalJson = paprikaRepository.findPriceByTokenAndDate(id, date)!!)
    }
}