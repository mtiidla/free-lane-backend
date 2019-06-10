package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.external.PoolInfoApi
import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.data.repository.model.SwimmingPoolOpeningHours
import ee.mtiidla.freelane.domain.CurrentWeek
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OpeningHoursService(
    private val poolRepository: SwimmingPoolRepository,
    private val poolInfoApi: PoolInfoApi
) {

    // TODO: marko 2019-06-02 use cases / services are allowed to use repository data models and
    // controller DTOs and should return controller view models, the plan is to ignore mapping to
    // domain entities since the only business logic is to deliver a rest api
    fun getForCurrentWeek(poolId: Long): List<SwimmingPoolOpeningHours> {
        val pool = checkNotNull(poolRepository.findByIdOrNull(poolId))
        val currentWeek = CurrentWeek.currentWeekAtZone(pool.time_zone)
        return poolInfoApi.getPoolOpeningHours(poolId, currentWeek.start, currentWeek.end)
            .sortedBy { it.date }
    }
}
