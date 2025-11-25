package perso.api.crypto.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.model.TransactionDto
import perso.api.crypto.service.TransactionService

@RestController
@RequestMapping("v1/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @GetMapping
    fun getAllTransactionsByUserId(): List<TransactionJson> {
        return transactionService.findAllTransactionsByUserId().map {
            TransactionJson(
                token = it.token,
                amount = it.amount.toString(),
                id = it.id,
                dateTransaction = it.dateTime
            )
        }
    }

    @Operation(summary = "Ajouter une transaction", description = "Permet d'enregistrer une transaction effectuée")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token trouvé"),
        ApiResponse(responseCode = "404", description = "Token non trouvé"),
        ApiResponse(responseCode = "500", description = "Erreur interne")
    ])
    @PostMapping
    fun postTransaction(@RequestBody transactionJson: TransactionJson): TransactionDto {
        return transactionService.saveTransaction(transactionJson)
    }

    @Operation(summary = "Supprimer une transaction", description = "Permet de supprimer une transaction effectuée")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token trouvé"),
        ApiResponse(responseCode = "404", description = "Token non trouvé"),
        ApiResponse(responseCode = "500", description = "Erreur interne")
    ])
    @DeleteMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteTransaction(@PathVariable id: String) {
        return transactionService.deleteTransaction(id)
    }

    @Operation(summary = "Modifier une transaction", description = "Permet de modifier une transaction effectuée")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token trouvé"),
        ApiResponse(responseCode = "404", description = "Token non trouvé"),
        ApiResponse(responseCode = "500", description = "Erreur interne")
    ])
    @PutMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun putTransaction(@RequestBody transactionJson: TransactionJson): TransactionJson {
        return TransactionJson.build(transactionService.modifyTransaction(transactionJson))
    }
}