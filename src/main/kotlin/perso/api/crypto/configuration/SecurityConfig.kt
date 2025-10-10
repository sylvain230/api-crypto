package perso.api.crypto.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthentificationFilter: JwtAuthentificationFilter, // Notre filtre personnalisé
    private val userDetailsService: UserDetailsService, // NOUVEAU
    private val passwordEncoder: PasswordEncoder // NOUVEAU
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .csrf { it.disable() } // Généralement désactivé pour les APIs stateless
            .sessionManagement {
                // CRUCIAL : Notre API est stateless (chaque requête contient le token)
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    // Permettre l'accès au login sans authentification
                    .requestMatchers("/auth/**").permitAll()
                    // Toutes les autres requêtes nécessitent un JWT valide
                    .anyRequest().authenticated()
            }
            .headers { headers ->
                headers
                    .frameOptions { it.disable() }
                    // On configure la politique de SameSite
                    .httpStrictTransportSecurity { it.includeSubDomains(true) }

                // La propriété SameSite est généralement configurée via le WebServerFactoryCustomizer
                // MAIS pour les cookies de session standard, on utilise la configuration du WebServer.

                // Pour être spécifique au jeton d'authentification dans un cookie HTTP-only (si on le créait ici)
                // on aurait besoin d'un filtre ou d'un bean dédié.

                // SOLUTION IDIOMATIQUE MODERNE (via la configuration du WebServer)
                // Vous devez vous assurer que Tomcat/Jetty est configuré pour ajouter le SameSite sur TOUS les cookies.

            }
            .exceptionHandling {
                // Ceci gère les requêtes non authentifiées (e.g. pas de token)
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            // Dernière étape : On ajoute notre filtre avant le filtre standard de Spring
            .addFilterBefore(
                jwtAuthentificationFilter,
                UsernamePasswordAuthenticationFilter::class.java)
            // Désactiver l'authentification HTTP de base
            .httpBasic { it.disable() }

        return http.build()
    }

    @Bean
    fun authentificationManager(config: AuthenticationConfiguration): AuthenticationManager {
        // Cette méthode demande à Spring de fournir l'implémentation standard.
        return config.authenticationManager
    }

    // Vous devez aussi vous assurer que le DaoAuthenticationProvider est bien configuré.
    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder)
        provider.setUserDetailsService(userDetailsService)
        return provider
    }
}