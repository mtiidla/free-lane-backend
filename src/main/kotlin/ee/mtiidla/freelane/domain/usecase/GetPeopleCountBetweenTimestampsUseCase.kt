package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.PeopleCountUnGrouper
import ee.mtiidla.freelane.data.repository.SwimmingPoolOpeningHoursRepository
import ee.mtiidla.freelane.data.repository.SwimmingPoolPeopleCountRepository
import ee.mtiidla.freelane.data.repository.model.SwimmingPoolPeopleCount
import ee.mtiidla.freelane.restapi.viewmodel.CountViewModel
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneOffset

@Component
class GetPeopleCountBetweenTimestampsUseCase(
    private val countRepository: SwimmingPoolPeopleCountRepository,
    private val openingHoursRepository: SwimmingPoolOpeningHoursRepository,
    private val peopleCountUnGrouper: PeopleCountUnGrouper
) {

    fun execute(request: Request): List<CountViewModel> {

        var startDate = request.startDate

        val openingHours = openingHoursRepository.findAllByPoolId(request.poolId)
        val allCounts = mutableListOf<SwimmingPoolPeopleCount>()

        while (!startDate.isAfter(request.endDate)) {
            val queryStart = startDate
            val queryEnd = request.endDate

            val groupedCount =
                countRepository.findAllByPoolIdAndDateBetween(request.poolId, queryStart, queryEnd)

            val dayOfWeek = startDate.dayOfWeek.value
            val openingHour = checkNotNull(openingHours.firstOrNull { it.dayOfWeek == dayOfWeek })
            // TODO: marko 2019-02-23 convert start date time to pool timezone?
            val filterStart = startDate.atTime(openingHour.open).toInstant(ZoneOffset.UTC)
            val filterEnd = startDate.atTime(openingHour.closed).toInstant(ZoneOffset.UTC)

            allCounts.addAll(groupedCount.flatMap(peopleCountUnGrouper::ungroup)
                .filter { filterEnd.isAfter(it.timestamp) && filterStart.isBefore(it.timestamp) })

            startDate = startDate.plusDays(1)
        }

        return allCounts.map {
            CountViewModel(it.timestamp, it.peopleCount)
        }
    }

    data class Request(val poolId: Long, val startDate: LocalDate, val endDate: LocalDate)

}
