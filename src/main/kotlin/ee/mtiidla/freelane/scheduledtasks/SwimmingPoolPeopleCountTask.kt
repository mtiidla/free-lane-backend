package ee.mtiidla.freelane.scheduledtasks

import ee.mtiidla.freelane.external.TeamBadeApi
import ee.mtiidla.freelane.model.SwimmingPoolGroupedPeopleCount
import ee.mtiidla.freelane.repository.SwimmingPoolPeopleCountRepository
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Component
class SwimmingPoolPeopleCountTask(
    private val service: TeamBadeApi,
    private val poolRepository: SwimmingPoolRepository,
    private val countRepository: SwimmingPoolPeopleCountRepository
) {

    @Scheduled(fixedRate = INTERVAL_MS)
    fun saveSwimmingPoolPeopleCount() {

        val date = LocalDate.now(ZoneOffset.UTC)

        poolRepository.findAll().forEach { pool ->

            val count = service.getPoolPeopleCount(pool.id)

            val model = countRepository.findByPoolIdAndDate(pool.id, date)
                ?: SwimmingPoolGroupedPeopleCount(poolId = pool.id, date = date)

            val timeCount =
                "${LocalTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)},$count;"
            val updated = timeCount + (model.timeCount.takeIf { value -> value.isNotEmpty() } ?: "")

            val newModel = model.copy(timeCount = updated)
            countRepository.save(newModel)
        }
    }

    companion object {
        const val INTERVAL_MS = 2 * 60 * 1000L
    }
}