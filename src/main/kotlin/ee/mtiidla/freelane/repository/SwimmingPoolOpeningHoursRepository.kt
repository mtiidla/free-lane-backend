package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPoolOpeningHours
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwimmingPoolOpeningHoursRepository : JpaRepository<SwimmingPoolOpeningHours, Long> {

    fun findAllByPoolId(poolId: Long) : List<SwimmingPoolOpeningHours>

}