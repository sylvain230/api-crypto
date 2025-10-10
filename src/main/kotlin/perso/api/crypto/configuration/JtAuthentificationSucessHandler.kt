package perso.api.crypto.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import perso.api.crypto.service.JwtService

@Component
class JwtAuthenticationSuccessHandler(
    private val jwtService: JwtService,
    private val jwtProperties: JwtProperties
)  {

    fun handleSuccessfulAuthentication(userId: Long, response: HttpServletResponse) {

        // 1. Générer le jeton
        val jwtToken = jwtService.generateToken(userId)

        // 2. Créer et configurer le Cookie HTTP-Only
        val authCookie = Cookie("access_token", jwtToken).apply {
            isHttpOnly = true
            maxAge = (jwtProperties.expiration / 1000).toInt()
            path = "/"
        }
        response.addCookie(authCookie)

        // 3. Définir le corps de la réponse JSON (propre)
        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_OK

        val responseBody = mapOf("userId" to userId, "message" to "Login successful")
        ObjectMapper().writeValue(response.writer, responseBody)
    }
}