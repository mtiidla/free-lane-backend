package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPool
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwimmingPoolRepository : JpaRepository<SwimmingPool, Long>