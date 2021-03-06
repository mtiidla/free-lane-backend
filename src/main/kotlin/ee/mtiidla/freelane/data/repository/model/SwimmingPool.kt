package ee.mtiidla.freelane.data.repository.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class SwimmingPool(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = "",
    val url: String = "",
    val cover_image_url: String = "",
    val address: String = "",
    val latitude: Float = 0F,
    val longitude: Float = 0F,
    val vemcount_key: String = "",
    val opening_hours_id: Long = 0L,
    val time_zone: String = "",
    val phone: String = ""
)
