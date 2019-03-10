package ee.mtiidla.freelane.model

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class SwimmingPoolGroupedPeopleCount(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val poolId: Long = 0,
    val date: LocalDate = LocalDate.MIN,
    val timeCount: String = ""
)
