package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPoolPeopleCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwimmingPoolPeopleCountRepository : JpaRepository<SwimmingPoolPeopleCount, Long>