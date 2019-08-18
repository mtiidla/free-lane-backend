package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.restapi.viewmodel.OpeningHoursViewModel
import ee.mtiidla.freelane.restapi.viewmodel.SwimmingPoolViewModel
import ee.mtiidla.freelane.restapi.viewmodel.mapper.SwimmingPoolViewModelMapper
import org.springframework.stereotype.Component

@Component
class GetAllSwimmingPoolsUseCase(
    private val repository: SwimmingPoolRepository,
    private val getOpeningHoursUseCase: GetRunning7DaysOpeningHoursUseCase,
    private val getLatestPeopleCountUseCase: GetLatestPeopleCountUseCase,
    private val viewModelMapper: SwimmingPoolViewModelMapper
) {

    fun execute(): List<SwimmingPoolViewModel> {
        return repository.findByOrderByNameAsc()
            .map { pool ->
                val hours = getOpeningHoursUseCase.execute(
                    GetRunning7DaysOpeningHoursUseCase.Request(pool.id)
                ).map {
                    OpeningHoursViewModel(
                        date = it.date.toString(),
                        open = it.open.toString(),
                        closed = it.closed.toString(),
                        extra = it.extra
                    )
                }
                val count = getLatestPeopleCountUseCase.execute(
                    GetLatestPeopleCountUseCase.Request(pool.id)
                )
                viewModelMapper.map(pool, hours, count)
            }
    }
}
