package perso.api.crypto.repository.http

import perso.api.crypto.repository.http.paprika.model.DataHistoricalJson
import perso.api.crypto.repository.http.paprika.model.InfosCoinJson
import perso.api.crypto.repository.http.paprika.model.TokenJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PaprikaApi {
    @GET("v1/coins/{id}")
    fun getInfosCoin(@Path("id") id: String): Call<InfosCoinJson>

    @GET("v1/tickers/{id}")
    fun getToken(@Path("id") id: String): Call<TokenJson>

    @GET("v1/tickers/{id}/historical")
    fun getPriceByIdAndDate(
        @Path("id") id: String,
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("interval") interval: String): Call<List<DataHistoricalJson>>
}