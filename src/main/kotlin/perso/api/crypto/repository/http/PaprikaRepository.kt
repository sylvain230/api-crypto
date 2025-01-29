package perso.api.crypto.repository.http


import org.springframework.stereotype.Repository
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.repository.http.paprika.model.DataHistoricalJson
import perso.api.crypto.repository.http.paprika.model.InfosCoinJson
import perso.api.crypto.repository.http.paprika.model.TokenJson
import perso.api.crypto.utils.RetrofitInstanceApiCoinPaprika
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Repository
class PaprikaRepository {
    val paprikaApi: PaprikaApi = RetrofitInstanceApiCoinPaprika.getInstance().create(PaprikaApi::class.java)

    fun getInformationTokenById(id: String): InfosCoinJson? {
        val response = paprikaApi.getInfosCoin(id).execute()
        return when(response.isSuccessful) {
            true -> response.body()
            false -> throw CryptoException("Une erreur s'est produite à la récupération du token $id.")
        }
    }

    fun getPriceTokensById(id: String): TokenJson? {
        val response = paprikaApi.getToken(id).execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw CryptoException("Une erreur s'est produite à la récupération du token $id.")
        }
    }

    fun findPriceByTokenAndDate(id: String, date: String): DataHistoricalJson? {
        val response = paprikaApi.getPriceByIdAndDate(
            id,
            date,
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).plusDays(1).toString(),
            "1d"
        ).execute()
        if (response.isSuccessful) {
            return response.body()?.first()
        } else {
            throw CryptoException("Une erreur s'est produite à la récupération du prix du token $id au $date.")
        }
    }
}