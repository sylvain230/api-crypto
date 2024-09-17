package perso.api.crypto.repository.http.paprika.model

class TokenDTO(
    val id: String,
    val name: String,
    val max_supply: Int,
    val quotes: QuotesDTO
)
