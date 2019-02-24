package ee.mtiidla.freelane.model

import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class SwimmingPoolGroupedPeopleCount(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val poolId: Long = 0,
    val year: Int = 0,
    val week: Int = 0,
    val weekDay: Int = 0,
    val timestampCount: String = ""
)
