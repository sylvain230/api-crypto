package perso.api.crypto.repository.http

import perso.api.crypto.repository.http.paprika.model.InfosCoinDTO
import perso.api.crypto.repository.http.paprika.model.TokenDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PaprikaApi {
    @GET("v1/coins/{id}")
    fun getInfosCoin(@Path("id") id: String): Call<InfosCoinDTO>

    @GET("v1/tickers/{id}")
    fun getToken(@Path("id") id: String): Call<TokenDTO>
}