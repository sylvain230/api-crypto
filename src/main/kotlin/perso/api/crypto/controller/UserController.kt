package perso.api.crypto.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import perso.api.crypto.model.LoginRequest
import perso.api.crypto.model.LoginResponse
import perso.api.crypto.model.RegisterRequestDto
import perso.api.crypto.service.JwtService
import perso.api.crypto.service.UserService

@RestController
@RequestMapping("auth/login")
class UserController(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @PostMapping( produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getToken(@RequestBody loginRequest: LoginRequest): LoginResponse {
        return userService.findByUsernameAndPassword(loginRequest)
    }

    @PostMapping("/register", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun registerNewUser(@RequestBody user: RegisterRequestDto) : LoginResponse {
        val newUser = userService.registerNewUser(user)
        val token = jwtService.generateToken(newUser.id)

        return LoginResponse(
            userId = newUser.id,
            token = token)
    }
}