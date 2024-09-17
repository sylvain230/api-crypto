package perso.api.crypto.model

import perso.api.crypto.repository.http.paprika.model.InfosCoinDTO

data class Coin(
    val id: String
) {
    companion object {
        fun build(infosCoinDTO: InfosCoinDTO): Coin {
           return Coin(infosCoinDTO.id)
        }
    }
}


