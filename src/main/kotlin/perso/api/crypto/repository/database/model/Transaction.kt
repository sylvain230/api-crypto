package perso.api.crypto.repository.database.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "transaction")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_transaction_id")
    var id: Long = 0,

    @Column(name = "token_id")
    val tokenId: String,

    @Column(name = "amount")
    val amount: Double,

    @Column(name = "datetime")
    val datetime: LocalDateTime,

    @Column(name = "price")
    val price: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    val appUser: AppUser
)