package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPool
import ee.mtiidla.freelane.model.SwimmingPoolPeopleCount
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import java.time.OffsetDateTime

@RunWith(SpringRunner::class)
@DataJpaTest
class SwimmingPoolPeopleCountRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var repository: SwimmingPoolPeopleCountRepository

    @Test
    fun savesPoolCount() {

        val pool = SwimmingPool(
            name = "Test Pool",
            vemcount_key = "Key",
            vemcount_stream_id = "1234"
        )

        val poolId = entityManager.persistAndGetId(pool) as Long

        val count = SwimmingPoolPeopleCount(
            poolId = poolId,
            timestamp = OffsetDateTime.now(),
            peopleCount = 25
        )

        entityManager.persist(count)

        val result = repository.findAllByPoolId(poolId)[0]

        assertEquals(count, result)

    }
}