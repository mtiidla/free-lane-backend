package ee.mtiidla.freelane.data.scheduledtasks

import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.domain.usecase.UpdateSwimmingPoolOpeningHoursUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SwimmingPoolOpeningHoursTask(
    private val updateHoursUseCase: UpdateSwimmingPoolOpeningHoursUseCase,
    private val poolRepository: SwimmingPoolRepository
) {

    @Scheduled(fixedRate = INTERVAL_MS)
    fun saveSwimmingPoolOpeningHours() {

        poolRepository.findAll().forEach { pool ->

            updateHoursUseCase.execute(UpdateSwimmingPoolOpeningHoursUseCase.Request(pool.id))
        }
    }

    companion object {
        const val INTERVAL_MS = 12 * 60 * 60 * 1000L // 12 hours
    }
}
