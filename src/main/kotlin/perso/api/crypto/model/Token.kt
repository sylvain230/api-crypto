package perso.api.crypto.model

import perso.api.crypto.repository.http.paprika.model.TokenDTO

data class Token(
    val athPrice: Double
) {
    companion object {
        fun build(tokenDTO: TokenDTO): Token {
            return Token(tokenDTO.quotes.USD.ath_price)
        }
    }
}