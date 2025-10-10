package perso.api.crypto.service

import org.springframework.stereotype.Service
import perso.api.crypto.controller.model.CryptoAssetJson
import perso.api.crypto.repository.database.TransactionRepository

@Service
class ResumeService(
    private val transactionRepository: TransactionRepository,
    private val tokenService: TokenService
) {

    fun getResumeCryptoByUserId(userId: String): List<CryptoAssetJson> {
        val result: MutableList<CryptoAssetJson> = mutableListOf()
        val transactionsByUserId = transactionRepository.findTransactionsByUserId(userId)


        val mapMontantByCypto: HashMap<String, Double> = HashMap()
        transactionsByUserId.forEach { transaction ->
            if (!mapMontantByCypto.containsKey(transaction.tokenId)) {
                mapMontantByCypto[transaction.tokenId] = 0.0
            }
            mapMontantByCypto[transaction.tokenId] = mapMontantByCypto[transaction.tokenId]!! + transaction.amount
        }

        mapMontantByCypto.forEach {
            val infoToken = tokenService.findPricesTokenById(it.key)
            result.add(
                CryptoAssetJson(
                    id = "${userId}_${it.key}",
                    name = it.key,
                    amount = it.value,
                    usdValue = infoToken.price * it.value,
                    percentageOfPortfolio = 0.0,
                    trend24h = infoToken.percent24h
                )
            )
        }
        return result
    }
}