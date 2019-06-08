package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import org.springframework.stereotype.Component

@Component
class DeleteSwimmingPoolUseCase(
    private val repository: SwimmingPoolRepository
) {

    fun execute(request: Request) {
        repository.deleteById(request.poolId)
    }

    data class Request(val poolId: Long)
}
