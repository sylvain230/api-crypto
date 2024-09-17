package perso.api.crypto.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import perso.api.crypto.model.Coin
import perso.api.crypto.model.Token
import perso.api.crypto.service.TokenService

@RestController
@RequestMapping("v1/tokens")
class TokenController(
    private val tokenService: TokenService
) {

    @GetMapping("/infos/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getInformationToken(@PathVariable id: String): Coin {
        return tokenService.findInformationsTokenById(id)
    }

    @GetMapping("/prices/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPricesToken(@PathVariable id: String): Token {
        return tokenService.findPricesTokenById(id)
    }
}