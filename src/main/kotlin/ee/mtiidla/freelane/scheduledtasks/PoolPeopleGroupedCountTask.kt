package ee.mtiidla.freelane.scheduledtasks

import ee.mtiidla.freelane.model.SwimmingPoolGroupedPeopleCount
import ee.mtiidla.freelane.repository.SwimmingPoolGroupedPeopleCountRepository
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.service.TeamBadePoolService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Component
class PoolPeopleGroupedCountTask(
    private val service: TeamBadePoolService,
    private val poolRepository: SwimmingPoolRepository,
    private val groupCountRepository: SwimmingPoolGroupedPeopleCountRepository
) {

    @Scheduled(fixedRate = INTERVAL_MS)
    fun saveSwimmingPoolGroupedPeopleCount() {

        val date = LocalDate.now(ZoneOffset.UTC)

        poolRepository.findAll().forEach {

            val poolId = it.id
            val count = service.getPoolPeopleCount(it.vemcount_key, it.vemcount_stream_id)

            val model = groupCountRepository.findByPoolIdAndDate(poolId, date)
                ?: SwimmingPoolGroupedPeopleCount(poolId = poolId, date = date)

            val timeCount =
                "${LocalTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)},$count;"
            val updated = timeCount + (model.timeCount.takeIf { value -> value.isNotEmpty() } ?: "")

            val newModel = model.copy(timeCount = updated)
            groupCountRepository.save(newModel)
        }
    }

    companion object {
        const val INTERVAL_MS = 2 * 60 * 1000L
    }
}