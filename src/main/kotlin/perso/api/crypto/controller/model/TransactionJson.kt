package perso.api.crypto.controller.model

data class TransactionJson(
    val token: String,
    val amount: Double,
    val date: String
)