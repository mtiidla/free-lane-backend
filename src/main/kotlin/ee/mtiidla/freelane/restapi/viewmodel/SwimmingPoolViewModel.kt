package ee.mtiidla.freelane.restapi.viewmodel

data class SwimmingPoolViewModel(
    val id: Long,
    val name: String,
    val url: String,
    val cover_image_url: String,
    val address: String,
    val latitude: Float,
    val longitude: Float,
    val time_zone: String,
    val latest_count: CountViewModel? = null,
    val opening_hours: List<OpeningHoursViewModel>? = null
)
