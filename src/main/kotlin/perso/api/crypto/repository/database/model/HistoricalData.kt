package perso.api.crypto.repository.database.model

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.*
import java.time.LocalDateTime
import org.hibernate.annotations.Type

@Entity
@Table(name = "historical_data")
data class HistoricalData (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_historical_data_id")
    var id: Long = 0,

    @Column(name = "token_id")
    val tokenId: String,

    @Type(JsonType::class)
    @Column(name = "historical", columnDefinition = "jsonb")
    var historical: String,

    @Column(name = "last_updated")
    var lastUpdated: LocalDateTime = LocalDateTime.now(),
)