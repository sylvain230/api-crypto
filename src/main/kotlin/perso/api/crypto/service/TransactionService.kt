package perso.api.crypto.service

import org.springframework.stereotype.Service
import perso.api.crypto.model.TransactionDto
import perso.api.crypto.repository.database.TransactionRepository

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {

    fun findAllTransactionsByUserId(userId: String): List<TransactionDto> {
        return transactionRepository.findTransactionsByUserId(userId).map { TransactionDto(
            token = it.tokenId,
            amount = it.amount,
            dateTime = it.datetime
        ) }
    }

}
