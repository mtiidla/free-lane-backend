package ee.mtiidla.freelane.domain.usecase

import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import org.springframework.stereotype.Service

@Service
class SwimmingPoolService(
    private val repository: SwimmingPoolRepository
) {

    // TODO: marko 2019-06-02 extract each controller method into use case or combine in this service
}