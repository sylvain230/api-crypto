package perso.api.crypto.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import perso.api.crypto.model.LoginRequest
import perso.api.crypto.model.LoginResponse
import perso.api.crypto.model.RegisterRequestDto
import perso.api.crypto.repository.database.UserRepository
import perso.api.crypto.repository.database.model.AppUser

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun findByUsernameAndPassword(loginRequest: LoginRequest): LoginResponse {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw RuntimeException("Nom d'utilisateur ou mot de passe incorrect.")

        if(passwordEncoder.matches(loginRequest.password, user.passwordHash)) {
            return LoginResponse(
                userId = user.id,
                token = jwtService.generateToken(user.id)
            )
        } else {
            throw RuntimeException("Nom d'utilisateur ou mot de passe incorrect.")
        }
    }

    fun registerNewUser(user: RegisterRequestDto): AppUser {

        val hashedPassword = passwordEncoder.encode(user.password)

        return userRepository.save(AppUser(
            username = user.username,
            passwordHash = hashedPassword
        ))
    }
}
