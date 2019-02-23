package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPoolPeopleCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface SwimmingPoolPeopleCountRepository : JpaRepository<SwimmingPoolPeopleCount, Long> {

    fun findAllByPoolId(poolId: Long) : List<SwimmingPoolPeopleCount>

    fun findFirst1ByPoolIdOrderByTimestampDesc(poolId: Long) : SwimmingPoolPeopleCount

    fun findAllByPoolIdAndTimestampBetween(poolId: Long, start: Instant, end: Instant) : List<SwimmingPoolPeopleCount>

}