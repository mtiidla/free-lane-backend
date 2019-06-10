package ee.mtiidla.freelane.data.repository

import ee.mtiidla.freelane.data.repository.model.SwimmingPoolOpeningHours
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.transaction.Transactional

@Repository
@Transactional
interface SwimmingPoolOpeningHoursRepository : JpaRepository<SwimmingPoolOpeningHours, Long> {

    // TODO: marko 2019-02-23 should maybe order by dayOfWeek 
    fun findAllByPoolIdAndDateBetweenOrderByDateAsc(
        poolId: Long,
        start: LocalDate,
        end: LocalDate
    ): List<SwimmingPoolOpeningHours>

    fun deleteByPoolIdAndDateBetween(poolId: Long, start: LocalDate, end: LocalDate)
}
