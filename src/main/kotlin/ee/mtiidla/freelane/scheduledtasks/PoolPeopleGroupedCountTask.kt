package ee.mtiidla.freelane.scheduledtasks

import ee.mtiidla.freelane.model.SwimmingPoolGroupedPeopleCount
import ee.mtiidla.freelane.repository.SwimmingPoolGroupedPeopleCountRepository
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.service.TeamBadePoolService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.time.temporal.WeekFields


@Component
class PoolPeopleGroupedCountTask(
    private val service: TeamBadePoolService,
    private val poolRepository: SwimmingPoolRepository,
    private val swimmingPoolGroupedPeopleCountRepository: SwimmingPoolGroupedPeopleCountRepository
) {

    @Scheduled(fixedRate = INTERVAL_MS)
    fun saveSwimmingGroupPoolPeopleCount() {
        poolRepository.findAll().forEach {
            val date = LocalDate.now()
            val weekFields = WeekFields.of(Locale.getDefault())

            val poolId = it.id
            val year = date.year
            val weekNumber = date.get(weekFields.weekOfWeekBasedYear())
            val weekDay = date.dayOfWeek.value
            val count = service.getPoolPeopleCount(it.vemcount_key, it.vemcount_stream_id)

            val model = swimmingPoolGroupedPeopleCountRepository.findByPoolIdAndYearAndWeekAndWeekDay(
                poolId = poolId,
                year = year,
                week = weekNumber,
                weekDay = weekDay
            ) ?: SwimmingPoolGroupedPeopleCount(
                poolId = poolId,
                year = year,
                week = weekNumber,
                weekDay = weekDay
            )

            val timestampCount = "${Instant.now().truncatedTo(ChronoUnit.SECONDS)},$count;"
            val updated = timestampCount+(model.timestampCount.takeIf { value -> value.isNotEmpty() } ?: "")

            val newModel = model.copy(timestampCount = updated)
            swimmingPoolGroupedPeopleCountRepository.save(newModel)
        }
    }

    companion object {
        const val INTERVAL_MS = 2 * 60 * 1000L
    }

}
