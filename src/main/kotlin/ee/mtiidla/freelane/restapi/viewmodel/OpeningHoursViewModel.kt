package ee.mtiidla.freelane.restapi.viewmodel

data class OpeningHoursViewModel(
    val date: String,
    val open: String,
    val closed: String,
    val extra: String?
)
