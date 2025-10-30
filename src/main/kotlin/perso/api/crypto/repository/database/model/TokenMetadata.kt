package perso.api.crypto.repository.database.model

import jakarta.persistence.*

@Entity
@Table(name = "token_metadata")
data class TokenMetadata (

    @Id
    @Column(name = "token_id")
    val tokenId: String,

    @Column(name = "symbol")
    val symbol: String,

    @Column(name = "name")
    val name: String,
)