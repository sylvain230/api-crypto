package perso.api.crypto.repository.database.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

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
    val amount: BigDecimal,

    @Column(name = "datetime")
    val datetime: LocalDate,

    @Column(name = "price")
    val price: BigDecimal
)