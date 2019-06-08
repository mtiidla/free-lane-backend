package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.restapi.dto.UpdateSwimmingPoolDto
import ee.mtiidla.freelane.restapi.viewmodel.SwimmingPoolViewModel
import ee.mtiidla.freelane.restapi.viewmodel.mapper.SwimmingPoolViewModelMapper
import org.springframework.stereotype.Component

@Component
class UpdateSwimmingPoolUseCase(
    private val repository: SwimmingPoolRepository,
    private val viewModelMapper: SwimmingPoolViewModelMapper
) {

    fun execute(request: Request): SwimmingPoolViewModel {
        val existing = repository.findById(request.poolId).orElseThrow {
            IllegalArgumentException("No pool with id: ${request.poolId}")
        }
        val updatedPool = with(request.updateSwimmingPoolDto) {
            existing.copy(
                name = name ?: existing.name,
                url = url ?: existing.url,
                address = address ?: existing.address,
                latitude = latitude ?: existing.latitude,
                longitude = longitude ?: existing.longitude,
                vemcount_key = vemcount_key ?: existing.vemcount_key,
                vemcount_stream_id = vemcount_stream_id ?: existing.vemcount_stream_id,
                opening_hours_id = opening_hours_id ?: existing.opening_hours_id,
                time_zone = time_zone ?: existing.time_zone
            )
        }
        return viewModelMapper.map(repository.save(updatedPool))
    }

    data class Request(val poolId: Long, val updateSwimmingPoolDto: UpdateSwimmingPoolDto)
}
