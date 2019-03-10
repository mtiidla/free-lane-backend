package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPoolGroupedPeopleCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface SwimmingPoolGroupedPeopleCountRepository : JpaRepository<SwimmingPoolGroupedPeopleCount, Long> {

    fun findByPoolIdAndDate(poolId: Long, date: LocalDate) : SwimmingPoolGroupedPeopleCount?

}
