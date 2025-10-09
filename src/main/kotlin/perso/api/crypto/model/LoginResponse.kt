package perso.api.crypto.model

data class LoginResponse(
    val token: String,
    val userId: Long,
)