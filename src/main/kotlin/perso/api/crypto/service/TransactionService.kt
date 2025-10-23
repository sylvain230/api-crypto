package perso.api.crypto.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.model.TransactionDto
import perso.api.crypto.repository.database.TransactionRepository
import perso.api.crypto.repository.database.model.Transaction
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val tokenService: TokenService,
    private val userService: UserService
) {

    fun findAllTransactionsByUserId(): List<TransactionDto> {
        val idUser = SecurityContextHolder.getContext().authentication.name
        return transactionRepository.findTransactionsByUserId(idUser).map { TransactionDto(
            token = it.tokenId,
            amount = it.amount,
            dateTime = it.datetime
        ) }
    }

    fun saveTransaction(transaction: TransactionJson): TransactionDto {
        val idUser = SecurityContextHolder.getContext().authentication.name

        return TransactionDto.build(transactionRepository.save(
                Transaction(
                tokenId = transaction.token,
                price = tokenService.getDetailsInformationTokenById(transaction.token).price,
                amount = transaction.amount,
                datetime = LocalDateTime.ofInstant(Instant.parse(transaction.date), ZoneId.of("Europe/Paris")),
                appUser = userService.findById(idUser = idUser)
                )
        ))
    }
}
