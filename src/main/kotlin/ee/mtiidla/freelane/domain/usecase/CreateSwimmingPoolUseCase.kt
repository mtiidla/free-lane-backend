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
    private val viewModelMapper: SwimmingPoolViewModelMapper
) {

    fun execute(request: Request) : SwimmingPoolViewModel {
        val swimmingPool = with(request.createSwimmingPoolDto) {
            SwimmingPool(
                name = name,
                url = url,
                cover_image_url = cover_image_url,
                address = address,
                latitude = latitude,
                longitude = longitude,
                vemcount_key = vemcount_key,
                vemcount_stream_id = vemcount_stream_id,
                opening_hours_id = opening_hours_id,
                time_zone = time_zone
            )
        }
        return viewModelMapper.map(repository.save(swimmingPool))
    }

    data class Request(val createSwimmingPoolDto: CreateSwimmingPoolDto)

}
