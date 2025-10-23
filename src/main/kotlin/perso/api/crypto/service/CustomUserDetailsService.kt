package perso.api.crypto.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import perso.api.crypto.repository.database.UserRepository

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {
        // 1. Appel à la base de données
        val user = userRepository.findById(userId.toLong())
            .orElseThrow { UsernameNotFoundException("Utilisateur non trouvé avec l'ID: $userId") }

        // 2. Conversion/Mapping vers l'objet standard de Spring Security
        val appUserDetails = User(
            user.id.toString(),
            user.passwordHash,
            emptyList()
        )

        // 3. Retourner l'objet UserDetails
        return appUserDetails
    }
}