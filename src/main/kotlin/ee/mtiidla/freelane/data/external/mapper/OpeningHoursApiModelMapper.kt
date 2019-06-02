package ee.mtiidla.freelane.data.external.mapper

import ee.mtiidla.freelane.data.external.model.OpeningHoursApiModel
import ee.mtiidla.freelane.data.repository.model.SwimmingPoolOpeningHours
import java.time.LocalDate
import java.time.LocalTime

object OpeningHoursApiModelMapper {

    fun map(apiModel: OpeningHoursApiModel): SwimmingPoolOpeningHours = with(apiModel) {
        SwimmingPoolOpeningHours(
            id = 0,
            poolId = 0,
            dayOfWeek = LocalDate.parse(date).dayOfWeek.value,
            open = LocalTime.parse(start_time),
            closed = LocalTime.parse(end_time)
        )
    }
}