package perso.api.crypto.service

import org.springframework.stereotype.Service
import perso.api.crypto.model.ChartDataPointDto
import perso.api.crypto.model.CryptoAssetDto
import perso.api.crypto.repository.database.TransactionRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ResumeService(
    private val transactionRepository: TransactionRepository,
    private val tokenService: TokenService
) {

    fun getResumeCryptoByUserId(userId: String): List<CryptoAssetDto> {
        val result: MutableList<CryptoAssetDto> = mutableListOf()
        val transactionsByUserId = transactionRepository.findTransactionsByUserId(userId)


        val mapMontantByCypto: HashMap<String, Double> = HashMap()
        transactionsByUserId.forEach { transaction ->
            if (!mapMontantByCypto.containsKey(transaction.tokenId)) {
                mapMontantByCypto[transaction.tokenId] = 0.0
            }
            mapMontantByCypto[transaction.tokenId] = mapMontantByCypto[transaction.tokenId]!! + transaction.amount
        }

        val tokens = tokenService.getSimpleInformationsTokenByIds(mapMontantByCypto.keys.joinToString(separator = ","))


        mapMontantByCypto.forEach { currentToken ->
            val infoToken = tokens.find { tokenInfo -> tokenInfo.name == currentToken.key }
            if (infoToken != null) {
                result.add(
                    CryptoAssetDto(
                        id = "${userId}_${currentToken.key}",
                        name = currentToken.key,
                        amount = currentToken.value,
                        usdValue = infoToken.price * currentToken.value,
                        percentageOfPortfolio = 0.0,
                        trend24h = infoToken.percent24h
                    )
                )
            }
        }
        return result
    }

    fun getPortfolioHistoryByUserId(userId: String) : List<ChartDataPointDto> {
        val mapHolding = mutableMapOf<String, Double>()
        val transactionsByUserId = transactionRepository.findTransactionsByUserId(userId)
        val dailyChartPoints = mutableMapOf<String, ChartDataPointDto>()

        if(transactionsByUserId.isEmpty()) return emptyList()

        transactionsByUserId.sortedBy { it.datetime }.forEach { transaction ->

            mapHolding.merge(transaction.tokenId, transaction.amount) { oldBalance, newAmount -> oldBalance + newAmount }
            var cumulativePortfolioValue = 0.0

            mapHolding.forEach { (tokenId, balance) ->
                val historicalPrice = tokenService.findPriceByTokenAndDate(tokenId, transaction.datetime.toLocalDate())
                 cumulativePortfolioValue += historicalPrice?.times(balance) ?: 0.0
            }
            val dateKey = formatToDayString(transaction.datetime)

            val finalPointForDate = ChartDataPointDto(
                date = dateKey,
                value = cumulativePortfolioValue,
            )

            dailyChartPoints[dateKey] = finalPointForDate
        }
        return dailyChartPoints.values.toList()
    }

    fun formatToDayString(dateTime: LocalDateTime): String {
        // Utilisez un formateur qui enlève l'heure, ex: dd/MM/yyyy
        return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}