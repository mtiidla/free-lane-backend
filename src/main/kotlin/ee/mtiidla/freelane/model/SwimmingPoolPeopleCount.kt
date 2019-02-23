package ee.mtiidla.freelane.model

import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class SwimmingPoolPeopleCount(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val poolId: Long = 0,
    val timestamp: Instant = Instant.EPOCH,
    val peopleCount: Int = 0
)