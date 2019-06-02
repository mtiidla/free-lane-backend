package ee.mtiidla.freelane.external.model

data class OpeningHoursApiModel(
    val date: String,
    val start_time: String,
    val end_time: String,
    val category_tid: String?,
    val notice: String?
)