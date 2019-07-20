package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.restapi.dto.UpdateSwimmingPoolDto
import ee.mtiidla.freelane.restapi.viewmodel.SwimmingPoolViewModel
import ee.mtiidla.freelane.restapi.viewmodel.mapper.SwimmingPoolViewModelMapper
import org.springframework.stereotype.Component

@Component
class UpdateSwimmingPoolUseCase(
    private val repository: SwimmingPoolRepository,
    private val viewModelMapper: SwimmingPoolViewModelMapper,
    private val updatePeopleCountUseCase: UpdateSwimmingPoolPeopleCountUseCase,
    private val updateOpeningHoursUseCase: UpdateSwimmingPoolOpeningHoursUseCase
) {

    fun execute(request: Request): SwimmingPoolViewModel {
        val poolId = request.poolId

        val existing = repository.findById(poolId).orElseThrow {
            IllegalArgumentException("No pool with id: $poolId")
        }
        val updatedPool = with(request.updateSwimmingPoolDto) {
            existing.copy(
                name = name?.trim() ?: existing.name,
                url = url?.trim() ?: existing.url,
                cover_image_url = cover_image_url?.trim() ?: existing.cover_image_url,
                address = address?.trim() ?: existing.address,
                latitude = latitude ?: existing.latitude,
                longitude = longitude ?: existing.longitude,
                vemcount_key = vemcount_key?.trim() ?: existing.vemcount_key,
                opening_hours_id = opening_hours_id ?: existing.opening_hours_id,
                time_zone = time_zone?.trim() ?: existing.time_zone,
                phone = phone?.trim() ?: existing.phone
            )
        }

        val pool = repository.save(updatedPool)

        if (updatedPool.vemcount_key != existing.vemcount_key) {
            updatePeopleCountUseCase.execute(UpdateSwimmingPoolPeopleCountUseCase.Request(poolId))
        }

        if (updatedPool.opening_hours_id != existing.opening_hours_id) {
            updateOpeningHoursUseCase.execute(UpdateSwimmingPoolOpeningHoursUseCase.Request(poolId))
        }
        return viewModelMapper.map(pool)
    }

    data class Request(val poolId: Long, val updateSwimmingPoolDto: UpdateSwimmingPoolDto)
}
