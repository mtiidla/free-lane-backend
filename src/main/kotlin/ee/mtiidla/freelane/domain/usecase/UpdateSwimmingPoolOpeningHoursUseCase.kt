package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.external.PoolInfoApi
import ee.mtiidla.freelane.data.repository.SwimmingPoolOpeningHoursRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneOffset

@Component
class UpdateSwimmingPoolOpeningHoursUseCase(
    private val service: PoolInfoApi,
    private val hoursRepository: SwimmingPoolOpeningHoursRepository
) {

    fun execute(request: Request) {
        // to avoid dealing with time zone difference between clients and server,
        // always store one extra day of opening hours in the past
        val yesterday = LocalDate.now(ZoneOffset.UTC).minusDays(1)

        val startDate = yesterday

        // TODO: marko 2019-06-10 how many days into the future clients need to show opening hours?
        val endDate = startDate.plusDays(30)

        val openingHours = service.getPoolOpeningHours(request.poolId, startDate, endDate)

        // TODO: marko 2019-06-10 find proper way to chain calls together with @Transactional?
        hoursRepository.deleteByPoolIdAndDateBetween(request.poolId, startDate, endDate)
        hoursRepository.saveAll(openingHours)
    }

    data class Request(val poolId: Long)
}
