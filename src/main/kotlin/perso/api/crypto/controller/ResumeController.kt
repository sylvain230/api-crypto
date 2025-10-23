package perso.api.crypto.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import perso.api.crypto.controller.model.ChartDataPointJson
import perso.api.crypto.controller.model.CryptoAssetJson
import perso.api.crypto.service.ResumeService

@RestController
@RequestMapping("v1/resume")
class ResumeController(
    private val resumeService: ResumeService
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getResumeAssetsByUserId(): List<CryptoAssetJson> {
        return CryptoAssetJson.buildList(resumeService.getResumeCryptoByUserId())
    }

    @GetMapping("chart/user", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getHistoryUser(): List<ChartDataPointJson> {
        return ChartDataPointJson.buildList(resumeService.getPortfolioHistoryByUserId())
    }
}