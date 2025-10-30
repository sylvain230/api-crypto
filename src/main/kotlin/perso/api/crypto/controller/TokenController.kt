package perso.api.crypto.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import perso.api.crypto.controller.model.TokenMetadataJson
import perso.api.crypto.controller.model.TransactionJson
import perso.api.crypto.model.*
import perso.api.crypto.service.TokenService

@RestController
@RequestMapping("v1/tokens")
class TokenController(
    private val tokenService: TokenService
) {

    @Operation(summary = "Détail d'un token", description = "Retourne le détail d'un token")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token trouvé"),
        ApiResponse(responseCode = "404", description = "Token non trouvé"),
        ApiResponse(responseCode = "500", description = "Erreur interne")
    ])
    @GetMapping("{id}/infos",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getDetailsInformationToken(@PathVariable id: String): TokenDetailsDto {
        return tokenService.getDetailsInformationTokenById(id)
    }

    @Operation(summary = "Calcul du profit par token", description = "Retourne le profit effectué pour chaque token")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token trouvé"),
        ApiResponse(responseCode = "404", description = "Token non trouvé"),
        ApiResponse(responseCode = "500", description = "Erreur interne")
    ])
    @GetMapping("{id}/profit")
    fun calculateProfitByToken(@PathVariable id: String): ResultDto {
        return tokenService.calculateProfitByToken(id)
    }

    @Operation(summary = "Ensemble des profits", description = "Retourne l'ensemble des profits effectués")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token trouvé"),
        ApiResponse(responseCode = "404", description = "Token non trouvé"),
        ApiResponse(responseCode = "500", description = "Erreur interne")
    ])
    @GetMapping("profit")
    fun getProfitByCoin(): List<ResultDto> {
        return tokenService.calculateProfit()
    }

//    @Operation(summary = "Détail d'un token par date", description = "Retourne la valeur d'un token pour une date donnée. Ne fonctionne que pour 1 an d'historique")
//    @ApiResponses(value = [
//        ApiResponse(responseCode = "200", description = "Token trouvé"),
//        ApiResponse(responseCode = "404", description = "Token non trouvé"),
//        ApiResponse(responseCode = "500", description = "Erreur interne")
//    ])
//    @GetMapping("{id}/historical")
//    fun getAveragePriceByTokenAndDate(@PathVariable id: String, @RequestParam date: String): DataHistoricalDto {
//        return tokenService.findPriceByTokenAndDate(id, date)
//    }

    @Operation(summary = "Profit total", description = "Calcul et affiche le profit total effectué sur l'ensemble des tokens")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token trouvé"),
        ApiResponse(responseCode = "404", description = "Token non trouvé"),
        ApiResponse(responseCode = "500", description = "Erreur interne")
    ])
    @GetMapping("totalProfit")
    fun getTotalProfit(): ProfitDto {
        return tokenService.calculateProfitTotal()
    }

    @GetMapping("all")
    fun getAllCoins(): List<TokenMetadataJson> {
        return tokenService.getAllCoins().map { TokenMetadataJson.build(it) }
    }
}