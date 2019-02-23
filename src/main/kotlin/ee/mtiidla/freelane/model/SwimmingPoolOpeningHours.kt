package ee.mtiidla.freelane.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Entity
data class SwimmingPoolOpeningHours(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val poolId: Long = 0,
    // TODO: marko 2019-02-23 add validation https://reflectoring.io/bean-validation-with-spring-boot/
    @Min(1, message = "Day of week starting from 1 as Mondady")
    @Max(7, message = "Last day of week is 7, Sunday")
    val dayOfWeek: Int = 0,
    val open: String = "",
    val closed: String = ""
)