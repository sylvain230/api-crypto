package perso.api.crypto.repository.http

import org.springframework.stereotype.Repository
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.repository.http.model.coingecko.CoinDetailsJson
import perso.api.crypto.repository.http.model.coingecko.MarketChartJson
import perso.api.crypto.repository.http.model.coingecko.TokenJson
import perso.api.crypto.utils.RetrofitInstanceApiCoinGecko

@Repository
class CoinGeckoRepository(
    val coinGeckoApi: CoinGeckoApi = RetrofitInstanceApiCoinGecko.getInstance().create(CoinGeckoApi::class.java)
) {

    fun getInformationTokenById(id: String): CoinDetailsJson? {
        val response = coinGeckoApi.getCoinDetails(id).execute()
        return when(response.isSuccessful) {
            true -> response.body()
            false -> throw CryptoException("Une erreur s'est produite à la récupération du token $id.")
        }
    }

    fun getPriceTokensById(id: String): Map<String, TokenJson>? {
        val response = coinGeckoApi.getPrices(id).execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw CryptoException("Une erreur s'est produite à la récupération du token $id.")
        }
    }

    fun getMarketChart(
        id: String): MarketChartJson? {
        val response = coinGeckoApi.getMarketChart(id).execute()
        if(response.isSuccessful) {
            return response.body()
        } else {
            throw CryptoException("Une erreur s'est produite à la récupération des données historiques de $id.")
        }
    }

//    fun findPriceByTokenAndDate(id: String, date: String): DataHistoricalJson? {
//        val response = coinGeckoApi.getPriceByIdAndDate(
//            id,
//            date,
//            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).plusDays(1).toString(),
//            "1d"
//        ).execute()
//        if (response.isSuccessful) {
//            return response.body()?.first()
//        } else {
//            throw CryptoException("Une erreur s'est produite à la récupération du prix du token $id au $date.")
//        }
//    }
}