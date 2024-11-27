package perso.api.crypto.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.model.*
import perso.api.crypto.service.TokenService

@RestController
@RequestMapping("v1/tokens")
class TokenController(
    private val tokenService: TokenService
) {

    @GetMapping("{id}/infos",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getInformationToken(@PathVariable id: String): CoinDto {
        return tokenService.findInformationTokenById(id)
    }

    @GetMapping("{id}/prices",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPricesToken(@PathVariable id: String): TokenDto {
        return tokenService.findPricesTokenById(id)
    }

    @PostMapping("/transaction")
    fun postAddTransaction(@RequestBody transaction: TransactionJson) {
        return tokenService.saveTransaction(transaction)
    }

    @GetMapping("{id}/profit")
    fun calculateProfitByToken(@PathVariable id: String): ResultDto {
        return tokenService.calculateProfitByToken(id)
    }
    
    @GetMapping("profit")
    fun getProfitByCoin(): List<ResultDto> {
        return tokenService.calculateProfit()
    }

    @GetMapping("{id}/historical")
    fun getAveragePriceByTokenAndDate(@PathVariable id: String, @RequestParam date: String): DataHistoricalDto {
        return tokenService.findPriceByTokenAndDate(id, date)
    }

    @GetMapping("totalProfit")
    fun getTotalProfit(): ProfitDto {
        return tokenService.calculateProfitTotal()
    }
}