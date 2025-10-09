package perso.api.crypto.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**") // S'applique à tous les chemins de l'API
                    .allowedOrigins("http://localhost:5174") // Autorise votre frontend
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes autorisées
                    .allowedHeaders("*") // Autorise tous les headers
                    .allowCredentials(true) // Autorise les cookies et les headers d'autorisation
            }
        }
    }
}