package perso.api.crypto.repository.database.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "transaction")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_transaction_id")
    var id: Long = 0,

    @Column(name = "amount")
    var amount: Double,

    @Column(name = "datetime")
    var datetime: Instant,

    @Column(name = "price")
    var price: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_id", nullable = false)
    val tokenMetadata: TokenMetadata,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    val appUser: AppUser
)