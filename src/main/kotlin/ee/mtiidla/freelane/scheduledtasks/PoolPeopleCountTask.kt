package ee.mtiidla.freelane.scheduledtasks

import ee.mtiidla.freelane.model.SwimmingPoolPeopleCount
import ee.mtiidla.freelane.repository.SwimmingPoolPeopleCountRepository
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.service.TeamBadePoolService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Component
class PoolPeopleCountTask(
    private val service: TeamBadePoolService,
    private val poolRepository: SwimmingPoolRepository,
    private val countRepository: SwimmingPoolPeopleCountRepository
) {

    @Scheduled(fixedRate = 120000)
    fun saveSwimmingPoolPeopleCount() {
        val pools = poolRepository.findAll()
        pools.forEach {
            val count = service.getPoolPeopleCount(it.vemcount_key, it.vemcount_stream_id)
            val peopleCount = SwimmingPoolPeopleCount(
                pool_id = it.id,
                // TODO: marko 2019-02-09 figure out proper way to approach time zones
                timestamp = OffsetDateTime.now(ZoneId.of("Europe/Copenhagen")).truncatedTo(ChronoUnit.SECONDS),
                people_count = count
            )
            countRepository.save(peopleCount)
        }
    }
}