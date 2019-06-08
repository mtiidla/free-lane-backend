package ee.mtiidla.freelane.restapi.dto

import ee.mtiidla.freelane.restapi.validation.ValidLocalDate

data class DateRangeDto(
    @ValidLocalDate val start_date: String,
    @ValidLocalDate val end_date: String
)
