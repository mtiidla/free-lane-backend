package ee.mtiidla.freelane.restapi.dto

import ee.mtiidla.freelane.restapi.validation.ValidTimeZone

data class UpdateSwimmingPoolDto(
    val name: String?,
    val url: String?,
    val cover_image_url: String?,
    val address: String?,
    val latitude: Float?,
    val longitude: Float?,
    val vemcount_key: String?,
    val opening_hours_id: Long?,
    val phone: String?,
    @ValidTimeZone val time_zone: String?
)
