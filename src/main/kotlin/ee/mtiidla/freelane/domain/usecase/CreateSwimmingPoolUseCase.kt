package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.data.repository.model.SwimmingPool
import ee.mtiidla.freelane.restapi.dto.CreateSwimmingPoolDto
import ee.mtiidla.freelane.restapi.viewmodel.SwimmingPoolViewModel
import ee.mtiidla.freelane.restapi.viewmodel.mapper.SwimmingPoolViewModelMapper
import org.springframework.stereotype.Component

@Component
class CreateSwimmingPoolUseCase(
    private val repository: SwimmingPoolRepository,
    private val updatePeopleCountUseCase: UpdateSwimmingPoolPeopleCountUseCase,
    private val updateOpeningHoursUseCase: UpdateSwimmingPoolOpeningHoursUseCase,
    private val viewModelMapper: SwimmingPoolViewModelMapper
) {

    fun execute(request: Request) : SwimmingPoolViewModel {
        val poolToCreate = with(request.createSwimmingPoolDto) {
            SwimmingPool(
                name = name.trim(),
                url = url.trim(),
                cover_image_url = cover_image_url.trim(),
                address = address.trim(),
                latitude = latitude,
                longitude = longitude,
                vemcount_key = vemcount_key.trim(),
                opening_hours_id = opening_hours_id,
                time_zone = time_zone.trim(),
                phone = phone.trim()
            )
        }
        val pool = repository.save(poolToCreate)
        updateOpeningHoursUseCase.execute(UpdateSwimmingPoolOpeningHoursUseCase.Request(pool.id))
        updatePeopleCountUseCase.execute(UpdateSwimmingPoolPeopleCountUseCase.Request(pool.id))

        return viewModelMapper.map(pool)
    }

    data class Request(val createSwimmingPoolDto: CreateSwimmingPoolDto)

}
