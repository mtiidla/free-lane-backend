package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.external.PoolInfoApi
import ee.mtiidla.freelane.data.repository.SwimmingPoolPeopleCountRepository
import ee.mtiidla.freelane.data.repository.model.SwimmingPoolGroupedPeopleCount
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Component
class UpdateSwimmingPoolPeopleCountUseCase(
    private val poolInfoApi: PoolInfoApi,
    private val countRepository: SwimmingPoolPeopleCountRepository
) {

    fun execute(request: Request) {

        val date = LocalDate.now(ZoneOffset.UTC)
        val poolId = request.poolId

        val count = poolInfoApi.getPoolPeopleCount(poolId)

        val model = countRepository.findByPoolIdAndDate(poolId, date)
            ?: SwimmingPoolGroupedPeopleCount(poolId = poolId, date = date)

        val timeCount =
            "${LocalTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)},$count;"
        val updated = timeCount + (model.timeCount.takeIf { value -> value.isNotEmpty() } ?: "")

        val newModel = model.copy(timeCount = updated)
        countRepository.save(newModel)
    }

    data class Request(val poolId: Long)
}
