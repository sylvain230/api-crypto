package perso.api.crypto.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtProperties(
    @Value("\${jwt.secret}")
    val secret: String,
    @Value("\${jwt.expiration}")
    val expiration: Long
)