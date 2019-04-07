package ee.mtiidla.freelane.scheduledtasks

import ee.mtiidla.freelane.model.SwimmingPoolGroupedPeopleCount
import ee.mtiidla.freelane.repository.SwimmingPoolPeopleCountRepository
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.service.TeamBadePoolService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Component
class SwimmingPoolPeopleCountTask(
    private val service: TeamBadePoolService,
    private val poolRepository: SwimmingPoolRepository,
    private val countRepository: SwimmingPoolPeopleCountRepository
) {

    @Scheduled(fixedRate = INTERVAL_MS)
    fun saveSwimmingPoolPeopleCount() {

        val date = LocalDate.now(ZoneOffset.UTC)

        poolRepository.findAll().forEach {

            val poolId = it.id
            val count = service.getPoolPeopleCount(it.vemcount_key, it.vemcount_stream_id)

            val model = countRepository.findByPoolIdAndDate(poolId, date)
                ?: SwimmingPoolGroupedPeopleCount(poolId = poolId, date = date)

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