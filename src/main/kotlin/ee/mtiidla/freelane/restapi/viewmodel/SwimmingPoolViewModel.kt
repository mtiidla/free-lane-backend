package ee.mtiidla.freelane.restapi.viewmodel

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
