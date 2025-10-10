package perso.api.crypto.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import perso.api.crypto.model.TransactionDto
import perso.api.crypto.service.TransactionService

@RestController
@RequestMapping("v1/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @GetMapping("{id}")
    fun getAllTransactionsByUserId(@PathVariable userId: String): List<TransactionDto> {
        return transactionService.findAllTransactionsByUserId(userId)
    }
}