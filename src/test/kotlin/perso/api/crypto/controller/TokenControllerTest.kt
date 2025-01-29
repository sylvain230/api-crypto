package perso.api.crypto.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import perso.api.crypto.configuration.ControllerTestConfig
import perso.api.crypto.model.CoinDto
import perso.api.crypto.service.TokenService

@WebMvcTest
@Import(ControllerTestConfig::class)
class TokenControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var tokenService: TokenService

    @Test
    fun testGetInformationToken() {

        every { tokenService.findInformationTokenById("btc-bitcoin") } returns CoinDto("btc-bitcoin")

        mockMvc.get("/v1/tokens/btc-bitcoin/infos") {
            content = MediaType.APPLICATION_JSON_VALUE
        }.andExpect {
            status { isOk() }
            content { json(mapper.writeValueAsString(CoinDto("btc-bitcoin"))) }
        }

        verify { tokenService.findInformationTokenById("btc-bitcoin") }
    }
}