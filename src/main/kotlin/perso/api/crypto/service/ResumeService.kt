package perso.api.crypto.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import perso.api.crypto.model.ChartDataPointDto
import perso.api.crypto.model.CryptoAssetDto
import perso.api.crypto.repository.database.TransactionRepository
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class ResumeService(
    private val transactionRepository: TransactionRepository,
    private val tokenService: TokenService
) {

    fun getResumeCryptoByUserId(): List<CryptoAssetDto> {
        val userId = SecurityContextHolder.getContext().authentication.name
        val result: MutableList<CryptoAssetDto> = mutableListOf()
        val transactionsByUserId = transactionRepository.findTransactionsByUserId(userId)


        val mapMontantByCypto: HashMap<String, Double> = HashMap()
        transactionsByUserId.forEach { transaction ->
            if (!mapMontantByCypto.containsKey(transaction.tokenMetadata.tokenId)) {
                mapMontantByCypto[transaction.tokenMetadata.tokenId] = 0.0
            }
            mapMontantByCypto[transaction.tokenMetadata.tokenId] = mapMontantByCypto[transaction.tokenMetadata.tokenId]!! + transaction.amount
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

    fun getPortfolioHistoryByUserId() : List<ChartDataPointDto> {
        val userId = SecurityContextHolder.getContext().authentication.name
        val mapHolding = mutableMapOf<String, Double>()
        val dailyChartPoints = mutableListOf<ChartDataPointDto>()

        val transactionsByUserId = transactionRepository.findTransactionsByUserId(userId)
        if(transactionsByUserId.isEmpty()) return emptyList()

        val mapData = tokenService.findYearlyHistoricalPrices(transactionsByUserId.map { it.tokenMetadata.tokenId }.distinct())

        val appZone = ZoneId.of("Europe/Paris")
        val transactionByDate = transactionsByUserId.groupBy { LocalDate.ofInstant(it.datetime, appZone)  }
        val today = LocalDate.now(appZone)
        val startDate = transactionsByUserId.first().datetime.atZone(appZone).toLocalDate()
        var currentDate = startDate

        while(!currentDate.isAfter(today)) {

            // Mise à jour des balances si des transactions ont eu lieu ce jour
            transactionByDate[currentDate]?.forEach { transaction ->
                mapHolding.merge(transaction.tokenMetadata.tokenId, transaction.amount) { oldBalance, newAmount -> oldBalance + newAmount }
            }

            // Calculer la valeur totale du portefeuille à la fin de cette journée
            var cumulativePortfolioValue = 0.0

            mapHolding.forEach { (tokenId, balance) ->
                // Récupère le prix pour ce token à cette date spécifique
                val historicalPrice = mapData[tokenId]?.get(currentDate)

                cumulativePortfolioValue += historicalPrice?.times(balance) ?: 0.0
            }

            // Ajouter le point de données (sauf si c'est la première date et la valeur est 0)
            dailyChartPoints.add(
                ChartDataPointDto(
                    date = formatToDayString(currentDate), // Assurez-vous que formatToDayString est précis
                    value = cumulativePortfolioValue
                )
            )

            // Passer au jour suivant
            currentDate = currentDate.plusDays(1)
        }

        return dailyChartPoints
    }

    fun formatToDayString(dateTime: LocalDate): String {
        // Utilisez un formateur qui enlève l'heure, ex: dd/MM/yyyy
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}