package ee.mtiidla.freelane.model

import java.time.Instant

data class SwimmingPoolPeopleCount(
    val poolId: Long,
    val timestamp: Instant,
    val peopleCount: Int
)