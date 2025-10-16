package perso.api.crypto.model.auth

data class RegisterRequestDto(
    val username: String,
    val password: String
)