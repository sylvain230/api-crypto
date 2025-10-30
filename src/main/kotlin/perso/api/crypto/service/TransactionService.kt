package perso.api.crypto.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.model.TransactionDto
import perso.api.crypto.repository.database.TokenMetadataRepository
import perso.api.crypto.repository.database.TransactionRepository
import perso.api.crypto.repository.database.model.Transaction
import java.time.ZoneId

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val tokenService: TokenService,
    private val userService: UserService,
    private val tokenMetadataRepository: TokenMetadataRepository
) {

    fun findAllTransactionsByUserId(): List<TransactionDto> {
        val idUser = SecurityContextHolder.getContext().authentication.name
        return transactionRepository.findTransactionsByUserId(idUser).map { TransactionDto(
            id = it.id,
            token = it.tokenMetadata.symbol,
            amount = it.amount,
            dateTime = it.datetime
        ) }
    }

    fun saveTransaction(transaction: TransactionJson): TransactionDto {
        val idUser = SecurityContextHolder.getContext().authentication.name

        val tokenMetadata = tokenMetadataRepository.findBySymbol(transaction.token)

        return TransactionDto.build(transactionRepository.save(
                Transaction(
                tokenMetadata = tokenMetadata,
                price = tokenService.getDetailsInformationTokenById(tokenMetadata.tokenId).price,
                amount = transaction.amount,
                datetime = transaction.dateTransaction,
                appUser = userService.findById(idUser = idUser)
                )
        ))
    }

    fun deleteTransaction(id: String) {
        return transactionRepository.deleteById(id.toInt())
    }

    fun modifyTransaction(transaction: TransactionJson): TransactionDto {
        val currentTransaction = transactionRepository.findById(transaction.id.toInt()).orElseThrow {
            CryptoException("Transaction avec ID ${transaction.id} non trouvée.")
        }
        currentTransaction.price = tokenService.getPriceByTokenAndDate(transaction.token, transaction.dateTransaction.atZone(ZoneId.of("Europe/Paris")).toLocalDate())
        currentTransaction.amount = transaction.amount
        currentTransaction.datetime = transaction.dateTransaction
        return TransactionDto.build(transactionRepository.save(currentTransaction))
    }
}
