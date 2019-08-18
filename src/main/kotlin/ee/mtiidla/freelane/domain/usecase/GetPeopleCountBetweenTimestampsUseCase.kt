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

        // TODO: marko 2019-08-18 Multiple opening hours are returned for each day in case of
        //  saunagus etc, should filter out only pool opening hours?
        val openingHours = openingHoursRepository.findAllByPoolIdAndDateBetweenOrderByDateAscIdAsc(
            request.poolId, startDate, request.endDate
        )
        val allCounts = mutableListOf<SwimmingPoolPeopleCount>()

        while (!startDate.isAfter(request.endDate)) {
            val queryStart = startDate
            val queryEnd = request.endDate

            val groupedCount =
                countRepository.findAllByPoolIdAndDateBetween(request.poolId, queryStart, queryEnd)

            // TODO: marko 2019-06-10 how to deal with counts for days we don't have opening hours for?
            val openingHour = openingHours.firstOrNull { it.date == startDate }
            // TODO: marko 2019-02-23 convert start date time to pool timezone?
            openingHour?.let { hour ->

                val filterStart = startDate.atTime(hour.open).toInstant(ZoneOffset.UTC)
                val filterEnd = startDate.atTime(hour.closed).toInstant(ZoneOffset.UTC)

                allCounts.addAll(groupedCount.flatMap(peopleCountUnGrouper::ungroup)
                    .filter { filterEnd.isAfter(it.timestamp) && filterStart.isBefore(it.timestamp) })
            }

            startDate = startDate.plusDays(1)
        }

        return allCounts.map {
            CountViewModel(it.timestamp, it.peopleCount)
        }
    }

    data class Request(val poolId: Long, val startDate: LocalDate, val endDate: LocalDate)
}
