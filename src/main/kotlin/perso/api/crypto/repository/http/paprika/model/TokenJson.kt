package perso.api.crypto.repository.http.paprika.model

data class TokenJson(
    val id: String,
    val name: String,
    val rank: String,
    val max_supply: Int,
    val quotes: QuotesJson
)
