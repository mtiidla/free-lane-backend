package ee.mtiidla.freelane.service

import ee.mtiidla.freelane.external.PoolInfoApi
import ee.mtiidla.freelane.model.SwimmingPoolOpeningHours
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.util.CurrentWeek
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OpeningHoursService(
    private val poolRepository: SwimmingPoolRepository,
    private val poolInfoApi: PoolInfoApi
) {

    fun getForCurrentWeek(poolId: Long): List<SwimmingPoolOpeningHours> {
        val pool = checkNotNull(poolRepository.findByIdOrNull(poolId))
        val currentWeek = CurrentWeek.currentWeekAtZone(pool.time_zone)
        return poolInfoApi.getPoolOpeningHours(poolId, currentWeek.start, currentWeek.end)
            .sortedBy { it.dayOfWeek }
    }
}