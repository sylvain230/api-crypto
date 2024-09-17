package perso.api.crypto.repository.http


import org.springframework.stereotype.Repository
import perso.api.crypto.repository.http.paprika.model.InfosCoinDTO
import perso.api.crypto.repository.http.paprika.model.TokenDTO
import perso.api.crypto.tools.RetrofitInstance

@Repository
class PaprikaRepository {
    val paprikaApi: PaprikaApi = RetrofitInstance.getInstance().create(PaprikaApi::class.java)

    fun getInformationsTokenById(id: String): InfosCoinDTO? {
        return paprikaApi.getInfosCoin(id).execute().body()
    }

    fun getPriceTokensById(id: String): TokenDTO? {
        return paprikaApi.getToken(id).execute().body()
    }
}