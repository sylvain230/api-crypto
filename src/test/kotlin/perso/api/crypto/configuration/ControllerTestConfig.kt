package perso.api.crypto.configuration

import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import okhttp3.mockwebserver.MockWebServer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import perso.api.crypto.repository.database.TransactionRepository
import perso.api.crypto.repository.http.PaprikaApi
import perso.api.crypto.repository.http.PaprikaRepository
import perso.api.crypto.service.TokenService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@TestConfiguration
class ControllerTestConfig {

    @MockK
    val transactionRepository = mockkClass(TransactionRepository::class)

    @Bean
    fun tokenService() = TokenService(
        paprikaRepository = PaprikaRepository(
            Retrofit.Builder()
            .baseUrl(mockServer().url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PaprikaApi::class.java)
        ),
        transactionRepository = transactionRepository
    )

    @Bean
    fun mockServer() = MockWebServer()
}