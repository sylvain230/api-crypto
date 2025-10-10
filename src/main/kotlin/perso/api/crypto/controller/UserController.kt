package perso.api.crypto.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import perso.api.crypto.configuration.JwtAuthenticationSuccessHandler
import perso.api.crypto.configuration.JwtProperties
import perso.api.crypto.model.LoginRequest
import perso.api.crypto.model.LoginResponse
import perso.api.crypto.model.RegisterRequestDto
import perso.api.crypto.service.JwtService
import perso.api.crypto.service.UserService

@RestController
@RequestMapping("auth/login")
class UserController(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val successHandler: JwtAuthenticationSuccessHandler
) {

    @PostMapping( produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getToken(@RequestBody loginRequest: LoginRequest,
                 request: HttpServletRequest,
                 response: HttpServletResponse
    ) {
        val user = userService.findByUsernameAndPassword(loginRequest)
        successHandler.handleSuccessfulAuthentication(user.userId, response)
    }

    @PostMapping("/register", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun registerNewUser(@RequestBody user: RegisterRequestDto) : LoginResponse {
        val newUser = userService.registerNewUser(user)

        return LoginResponse(
            userId = newUser.id)
    }
}