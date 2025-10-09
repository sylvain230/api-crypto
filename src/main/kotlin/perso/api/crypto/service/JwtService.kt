package perso.api.crypto.service

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import perso.api.crypto.configuration.JwtProperties
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {

    private val key = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    private val parser = Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray(StandardCharsets.UTF_8)))
        .build()

    fun generateToken(userId: Long): String {
        val now = Instant.now()
        val expirationTime = now.plusMillis(jwtProperties.expiration)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expirationTime))
            .signWith(key)
            .compact()
    }

    fun validateTokenAndExtractUsername(token: String): Long? {
        return try {
            val claims = parser.parseClaimsJws(token).body

            claims.subject.toLong()
        } catch (e: JwtException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}