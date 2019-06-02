package ee.mtiidla.freelane.data.external

import ee.mtiidla.freelane.data.repository.model.SwimmingPoolOpeningHours
import java.time.LocalDate

interface PoolInfoApi {

    fun getPoolPeopleCount(poolId: Long): Int

    fun getPoolOpeningHours(
        poolId: Long,
        fromDate: LocalDate,
        toDate: LocalDate
    ): List<SwimmingPoolOpeningHours>
}