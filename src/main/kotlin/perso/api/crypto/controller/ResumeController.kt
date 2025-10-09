package perso.api.crypto.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import perso.api.crypto.controller.model.CryptoAssetJson
import perso.api.crypto.service.ResumeService

@RestController
@RequestMapping("v1/resume")
class ResumeController(
    private val resumeService: ResumeService
) {

    @GetMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getResumeAssetesByUserId(@PathVariable id : String): List<CryptoAssetJson> {
        return resumeService.getResumeCryptoByUserId(id)
    }
}