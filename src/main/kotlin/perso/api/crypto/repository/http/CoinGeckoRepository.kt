package perso.api.crypto.repository.http

import org.springframework.stereotype.Repository
import perso.api.crypto.exception.CryptoException
import perso.api.crypto.repository.http.model.coingecko.CoinDetailsJson
import perso.api.crypto.repository.http.model.coingecko.HistoryJson
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
            false -> {
                val code = response.code()
                throw CryptoException("Une erreur $code s'est produite à la récupération du token $id.")
            }
        }
    }

    fun getPriceTokensById(id: String): Map<String, TokenJson>? {
        val response = coinGeckoApi.getPrices(id).execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            val code = response.code()
            throw CryptoException("Une erreur $code s'est produite à la récupération du token $id.")
        }
    }

    fun getMarketChart(
        id: String): MarketChartJson? {
        val response = coinGeckoApi.getMarketChart(id).execute()
        if(response.isSuccessful) {
            return response.body()
        } else {
            val code = response.code()
            throw CryptoException("Une erreur $code s'est produite à la récupération des données historiques de $id.")
        }
    }

    fun getPriceByTokenAndDate(id: String, date: String): HistoryJson? {
        val response = coinGeckoApi.getHistoryByTokenAndDate(
            id,
            date
        ).execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            val code = response.code()
            throw CryptoException("Une erreur $code s'est produite à la récupération du prix du token $id au $date.")
        }
    }
}