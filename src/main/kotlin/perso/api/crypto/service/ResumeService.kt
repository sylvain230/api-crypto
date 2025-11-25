package perso.api.crypto.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.model.ChartDataPointDto
import perso.api.crypto.model.CryptoAssetDto
import perso.api.crypto.model.ResumeDto
import perso.api.crypto.repository.database.TransactionRepository
import java.math.BigDecimal
import java.math.RoundingMode
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
        val cryptoAssets: MutableList<CryptoAssetDto> = mutableListOf()
        val transactionsByUserId = transactionRepository.findTransactionsByUserId(userId)
        var totalAmount = BigDecimal.ZERO


        val mapMontantByCypto: HashMap<String, BigDecimal> = HashMap()
        transactionsByUserId.forEach { transaction ->
            totalAmount = totalAmount.add(transaction.amount)
            if (!mapMontantByCypto.containsKey(transaction.tokenMetadata.tokenId)) {
                mapMontantByCypto[transaction.tokenMetadata.tokenId] = BigDecimal.ZERO
            }
            mapMontantByCypto[transaction.tokenMetadata.tokenId] = mapMontantByCypto[transaction.tokenMetadata.tokenId]!!.add(transaction.amount)
        }

        val tokens = tokenService.getSimpleInformationsTokenByIds(mapMontantByCypto.keys.joinToString(separator = ","))


        mapMontantByCypto.forEach { currentToken ->
            val infoToken = tokens.find { tokenInfo -> tokenInfo.name == currentToken.key }
            if (infoToken != null) {
                cryptoAssets.add(
                    CryptoAssetDto(
                        id = "${userId}_${currentToken.key}",
                        name = currentToken.key,
                        amount = currentToken.value,
                        usdValue = infoToken.price * currentToken.value,
                        percentageOfPortfolio = currentToken.value.multiply(BigDecimal(100.0)).divide(totalAmount, 2, RoundingMode.HALF_UP).setScale(2),
                        trend24h = infoToken.percent24h
                    )
                )
            }
        }
        return cryptoAssets
    }

    fun getPortfolioHistoryByUserId() : List<ChartDataPointDto> {
        val userId = SecurityContextHolder.getContext().authentication.name
        val mapHolding = mutableMapOf<String, BigDecimal>()
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
            var cumulativePortfolioValue = BigDecimal.ZERO

            mapHolding.forEach { (tokenId, balance) ->
                // Récupère le prix pour ce token à cette date spécifique
                val historicalPrice = mapData[tokenId]?.get(currentDate)

                cumulativePortfolioValue = cumulativePortfolioValue.add(historicalPrice?.multiply(balance) ?: BigDecimal.ZERO)
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

    fun getGlobalResumeByUserId(): ResumeDto {
        val userId = SecurityContextHolder.getContext().authentication.name
        val transactions = transactionRepository.findTransactionsByUserId(userId)

        if (transactions.isEmpty()) {
            return ResumeDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
        }

        val uniqueTokenIds = transactions.map { it.tokenMetadata.tokenId }.distinct()
        val mapTokenPriceToday = uniqueTokenIds.associate { tokenId ->
            tokenService.findPriceByTokenAndDate(tokenId, LocalDate.now()).let { price -> tokenId to price }
        }

        var totalInvest = BigDecimal.ZERO
        var amountToday = BigDecimal.ZERO

        transactions.forEach { transaction ->
            val priceToday = mapTokenPriceToday[transaction.tokenMetadata.tokenId]

            if(priceToday != null) {
                totalInvest += transaction.amount.multiply(transaction.price)
                amountToday += transaction.amount.multiply(priceToday)
            }
        }

        if (totalInvest.compareTo(BigDecimal.ZERO) == 0) {
            return ResumeDto(totalInvest, amountToday, BigDecimal.ZERO, BigDecimal.ZERO)
        }

        val amountWinOrloss = amountToday.subtract(totalInvest)
        val percentWinOrLoss = amountWinOrloss
            .multiply(BigDecimal(100))
            .divide(totalInvest, 2, RoundingMode.HALF_UP)

        return ResumeDto(
            totalInvest = totalInvest,
            amountToday = amountToday,
            amountWinOrLoss = amountWinOrloss,
            percentWinOrLoss = percentWinOrLoss
        )
    }
}