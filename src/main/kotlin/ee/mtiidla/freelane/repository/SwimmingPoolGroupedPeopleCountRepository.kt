package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPoolGroupedPeopleCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwimmingPoolGroupedPeopleCountRepository : JpaRepository<SwimmingPoolGroupedPeopleCount, Long> {

    fun findByPoolIdAndYearAndWeekAndWeekDay(poolId: Long, year: Int, week: Int, weekDay: Int) : SwimmingPoolGroupedPeopleCount?

}
