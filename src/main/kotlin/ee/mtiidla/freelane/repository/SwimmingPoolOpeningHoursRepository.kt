package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPoolOpeningHours
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwimmingPoolOpeningHoursRepository : JpaRepository<SwimmingPoolOpeningHours, Long> {

    // TODO: marko 2019-02-23 should maybe order by dayOfWeek 
    fun findAllByPoolId(poolId: Long) : List<SwimmingPoolOpeningHours>

}