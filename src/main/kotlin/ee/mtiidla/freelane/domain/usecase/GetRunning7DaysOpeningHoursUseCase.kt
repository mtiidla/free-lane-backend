package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.SwimmingPoolOpeningHoursRepository
import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.data.repository.model.SwimmingPoolOpeningHours
import ee.mtiidla.freelane.domain.DateRangeFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class GetRunning7DaysOpeningHoursUseCase(
    private val poolRepository: SwimmingPoolRepository,
    private val hoursRepository: SwimmingPoolOpeningHoursRepository
) {
    fun execute(request: Request): List<SwimmingPoolOpeningHours> {
        val poolId = request.poolId
        val pool = checkNotNull(poolRepository.findByIdOrNull(poolId))
        val dateRange = DateRangeFactory.running7DaysAtZone(pool.time_zone)
        return hoursRepository.findAllByPoolIdAndDateBetweenOrderByDateAsc(
            poolId = request.poolId,
            start = dateRange.start, end = dateRange.end
        ).sortedBy { it.date }
    }

    data class Request(val poolId: Long)
}
