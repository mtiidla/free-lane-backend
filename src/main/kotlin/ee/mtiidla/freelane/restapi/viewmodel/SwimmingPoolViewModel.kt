package ee.mtiidla.freelane.restapi.viewmodel

import java.time.Instant

data class SwimmingPoolViewModel(
    val id: Long,
    val name: String,
    val url: String,
    val address: String,
    val latitude: Float,
    val longitude: Float,
    val latest_count: CountViewModel,
    val opening_hours: List<OpeningHoursViewModel>
)

data class CountViewModel(
    val timestamp: Instant,
    val people: Int
)

data class OpeningHoursViewModel(
    val day: Int,
    val open: String,
    val closed: String
)