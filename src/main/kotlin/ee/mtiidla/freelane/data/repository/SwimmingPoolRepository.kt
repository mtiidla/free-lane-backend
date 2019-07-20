package ee.mtiidla.freelane.data.repository

import ee.mtiidla.freelane.data.repository.model.SwimmingPool
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwimmingPoolRepository : JpaRepository<SwimmingPool, Long> {

    fun findByOrderByNameDesc() : List<SwimmingPool>
}
