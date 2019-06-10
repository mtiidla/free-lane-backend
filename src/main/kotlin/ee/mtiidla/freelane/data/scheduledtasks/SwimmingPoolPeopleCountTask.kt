package ee.mtiidla.freelane.data.scheduledtasks

import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.domain.usecase.UpdateSwimmingPoolPeopleCountUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SwimmingPoolPeopleCountTask(
    private val poolRepository: SwimmingPoolRepository,
    private val updatePeopleCountUseCase: UpdateSwimmingPoolPeopleCountUseCase
) {

    @Scheduled(fixedRate = INTERVAL_MS)
    fun saveSwimmingPoolPeopleCount() {

        poolRepository.findAll().forEach { pool ->

            updatePeopleCountUseCase.execute(UpdateSwimmingPoolPeopleCountUseCase.Request(pool.id))
        }
    }

    companion object {
        const val INTERVAL_MS = 2 * 60 * 1000L // 2 minutes
    }
}
