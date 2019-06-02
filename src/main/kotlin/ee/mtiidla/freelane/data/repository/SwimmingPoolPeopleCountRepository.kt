package ee.mtiidla.freelane.data.repository

import ee.mtiidla.freelane.data.repository.model.SwimmingPoolGroupedPeopleCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface SwimmingPoolPeopleCountRepository : JpaRepository<SwimmingPoolGroupedPeopleCount, Long> {

    fun findByPoolIdAndDate(poolId: Long, date: LocalDate): SwimmingPoolGroupedPeopleCount?

    fun findAllByPoolIdAndDateBetween(
        poolId: Long,
        start: LocalDate,
        end: LocalDate
    ): List<SwimmingPoolGroupedPeopleCount>

    fun findFirst1ByPoolIdOrderByDateDesc(poolId: Long): SwimmingPoolGroupedPeopleCount?
}
