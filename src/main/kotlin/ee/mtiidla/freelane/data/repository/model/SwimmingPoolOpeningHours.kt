package ee.mtiidla.freelane.data.repository.model

import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class SwimmingPoolOpeningHours(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val poolId: Long = 0,
    val date: LocalDate = LocalDate.MIN,
    val open: LocalTime = LocalTime.MIDNIGHT,
    val closed: LocalTime = LocalTime.MIDNIGHT,
    val extra: String? = null
)
