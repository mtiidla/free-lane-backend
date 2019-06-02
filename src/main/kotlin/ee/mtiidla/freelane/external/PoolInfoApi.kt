package ee.mtiidla.freelane.external

import ee.mtiidla.freelane.model.SwimmingPoolOpeningHours
import java.time.LocalDate

interface PoolInfoApi {

    fun getPoolPeopleCount(poolId: Long): Int

    fun getPoolOpeningHours(
        poolId: Long,
        fromDate: LocalDate,
        toDate: LocalDate
    ): List<SwimmingPoolOpeningHours>
}