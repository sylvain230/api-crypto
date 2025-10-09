package perso.api.crypto.repository.database.model

import jakarta.persistence.*

@Entity
@Table(name = "app_user")
class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_user_id")
    var id: Long = 0,

    @Column(name = "username", nullable = false, unique = true)
    var username: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,
) {

    @OneToMany(
        mappedBy = "appUser",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val transactions: MutableSet<Transaction> = mutableSetOf()

}