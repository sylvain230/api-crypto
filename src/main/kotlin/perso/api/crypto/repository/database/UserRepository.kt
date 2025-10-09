package perso.api.crypto.repository.database

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import perso.api.crypto.repository.database.model.AppUser
import java.util.*

@Repository
interface UserRepository : CrudRepository<AppUser, Long> {

    fun findByUsername(username: String): AppUser?

    override fun findById(id: Long): Optional<AppUser>

}