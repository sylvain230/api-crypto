package perso.api.crypto.configuration

import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import perso.api.crypto.service.TokenService

class ControllerTestConfig {
    @Bean
    fun tokenService() = mockk<TokenService>()
}