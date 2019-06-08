package ee.mtiidla.freelane.restapi.viewmodel.mapper

import ee.mtiidla.freelane.data.repository.model.SwimmingPool
import ee.mtiidla.freelane.restapi.viewmodel.CountViewModel
import ee.mtiidla.freelane.restapi.viewmodel.OpeningHoursViewModel
import ee.mtiidla.freelane.restapi.viewmodel.SwimmingPoolViewModel
import org.springframework.stereotype.Component

@Component
class SwimmingPoolViewModelMapper : ViewModelMapper<SwimmingPool, SwimmingPoolViewModel> {

    override fun map(item: SwimmingPool): SwimmingPoolViewModel = with(item) {
        SwimmingPoolViewModel(
            id = id,
            name = name,
            url = url,
            cover_image_url = cover_image_url,
            address = address,
            latitude = latitude,
            longitude = longitude,
            time_zone = time_zone
        )
    }

    fun map(
        item: SwimmingPool,
        openingHours: List<OpeningHoursViewModel>?,
        latestCount: CountViewModel?
    ): SwimmingPoolViewModel {
        return map(item).copy(latest_count = latestCount, opening_hours = openingHours)
    }
}
