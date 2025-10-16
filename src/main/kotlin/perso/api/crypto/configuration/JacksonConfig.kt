package perso.api.crypto.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    @Bean
    fun mapper(): ObjectMapper {
        return ObjectMapper().registerModules(mutableListOf(JavaTimeModule(), KotlinModule.Builder().build()))
    }
}