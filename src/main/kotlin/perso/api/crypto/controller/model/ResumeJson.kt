package perso.api.crypto.controller.model

import perso.api.crypto.model.ResumeDto

data class ResumeJson(
    val totalInvest: String,
    val amountToday: String,
    val percentWinOrLoss: String,
    val amountWinOrLoss: String
) {
    companion object {
        fun build(resumeDto: ResumeDto): ResumeJson {
            return ResumeJson(
                totalInvest = resumeDto.totalInvest.toString(),
                amountToday = resumeDto.amountToday.toString(),
                percentWinOrLoss = resumeDto.percentWinOrLoss.toString(),
                amountWinOrLoss = resumeDto.amountWinOrLoss.toString()
            )
        }
    }
}