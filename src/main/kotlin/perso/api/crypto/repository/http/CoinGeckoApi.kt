package perso.api.crypto.repository.http

import perso.api.crypto.repository.http.model.coingecko.CoinDetailsJson
import perso.api.crypto.repository.http.model.coingecko.HistoryJson
import perso.api.crypto.repository.http.model.coingecko.MarketChartJson
import perso.api.crypto.repository.http.model.coingecko.TokenJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("api/v3/coins/{id}")
    fun getCoinDetails(@Path("id") id: String): Call<CoinDetailsJson>

    @GET("api/v3/simple/price")
    fun getPrices(
        @Query("ids") ids: String,
        @Query("vs_currencies") currency: String = "usd",
        @Query("include_24hr_change") oneDayChange: Boolean = true
    ): Call<Map<String, TokenJson>>

    @GET("api/v3/coins/{id}/market_chart")
    fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: Int = 365,
        @Query("interval") interval: String = "daily"
    ): Call<MarketChartJson>

    @GET("api/v3/coins/{id}/history")
    fun getHistoryByTokenAndDate(
        @Path("id") id: String,
        @Query("date") date: String
    ): Call<HistoryJson>
}