package perso.api.crypto.configuration

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import perso.api.crypto.service.CustomUserDetailsService
import perso.api.crypto.service.JwtService

@Component
class JwtAuthentificationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() { // Extension clé pour s'assurer que le filtre ne s'exécute qu'une fois par requête
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. Extraction de l'en-tête
        val authHeader = request.getHeader("Authorization")

        // Si l'en-tête est manquante ou ne commence pas par Bearer, on passe la main.
        if(authHeader.isNullOrEmpty() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader.substring(7)

        // 2. Validation du token et extraction de l'ID
        val userId = jwtService.validateTokenAndExtractUsername(token = jwtToken)

        // Vérification
        // 1. L'ID est présent
        // 2. L'utilisateur n'est PAS déjà authentifié dans le contexte actuel (pour éviter le re-traitement)
        if(userId != null && SecurityContextHolder.getContext().authentication == null) {

            // 3. Charger l'utilisateur et Créer le Token Auth.
            val userDetails = userDetailsService.loadUserByUsername(userId.toString())

            // Création du Token d'Authentification interne de Spring
            val authToken = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

            // 4. Mettre à jour le Contexte de Sécurité
            val context = SecurityContextHolder.createEmptyContext()
            context.authentication = authToken
            SecurityContextHolder.setContext(context)
        }

        // On continue la chaîne de filtres (très important !)
        filterChain.doFilter(request, response)
    }
}