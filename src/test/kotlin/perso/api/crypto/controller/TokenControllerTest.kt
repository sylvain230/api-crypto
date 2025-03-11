package perso.api.crypto.controller

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import perso.api.crypto.configuration.ControllerTestConfig
import java.io.File


@WebMvcTest
@Import(ControllerTestConfig::class)
class TokenControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    @Qualifier("mockServer")
    lateinit var mockServer: MockWebServer

    private val btcBitcoinResult = File("src/test/resources/mock/apicrypto/btcBitcoin.json")
    private val getTokenBtcBitcoinJson = File("src/test/resources/mock/apicoinpaprika/getTokenBtcBitcoin.json")

    @Test
    fun testGetPricesToken() {

        val mockResponse = MockResponse()
        mockResponse.setBody(getTokenBtcBitcoinJson.readText())
        mockServer.enqueue(mockResponse)

        val expected = btcBitcoinResult.readText()

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/tokens/btc-bitcoin/prices"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected, true))
    }
}