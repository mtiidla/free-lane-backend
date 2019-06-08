package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.PeopleCountUnGrouper
import ee.mtiidla.freelane.data.repository.SwimmingPoolPeopleCountRepository
import ee.mtiidla.freelane.restapi.viewmodel.CountViewModel
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class GetLatestPeopleCountUseCase(
    private val countRepository: SwimmingPoolPeopleCountRepository,
    private val peopleCountUnGrouper: PeopleCountUnGrouper
) {

    fun execute(request: Request): CountViewModel {
        val peopleCount = countRepository.findFirst1ByPoolIdOrderByDateDesc(request.poolId)
        if (peopleCount != null) {
            val latestCount = peopleCountUnGrouper.ungroupFirst(peopleCount)
            if (latestCount != null) {
                return CountViewModel(latestCount.timestamp, latestCount.peopleCount)
            }
        }
        return CountViewModel(Instant.now(), 0)
    }

    data class Request(val poolId: Long)

}
