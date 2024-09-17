package perso.api.crypto.service

import org.springframework.stereotype.Service
import perso.api.crypto.model.Coin
import perso.api.crypto.model.Token
import perso.api.crypto.repository.http.PaprikaRepository

@Service
class TokenService(
    private val paprikaRepository: PaprikaRepository
) {

    fun findInformationsTokenById(id: String): Coin {
        return Coin.build(infosCoinDTO = paprikaRepository.getInformationsTokenById(id)!!)
    }

    fun findPricesTokenById(id: String) : Token {
        return Token.build(tokenDTO = paprikaRepository.getPriceTokensById(id)!!)
    }
}