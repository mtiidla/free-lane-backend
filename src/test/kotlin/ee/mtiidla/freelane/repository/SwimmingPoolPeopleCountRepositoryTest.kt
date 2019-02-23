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
import java.time.Instant
import java.time.temporal.ChronoUnit

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
            timestamp = Instant.now(),
            peopleCount = 25
        )

        entityManager.persist(count)

        val result = repository.findAllByPoolId(poolId)[0]

        assertEquals(count, result)

    }


    @Test
    fun latestTimestampByPoolId() {

        val pool = SwimmingPool(
            name = "Test Pool",
            vemcount_key = "Key",
            vemcount_stream_id = "1234"
        )

        val poolId = entityManager.persistAndGetId(pool) as Long

        val count = SwimmingPoolPeopleCount(
            poolId = poolId,
            timestamp = Instant.now(),
            peopleCount = 25
        )

        entityManager.persist(count)

        val result = repository.findFirst1ByPoolIdOrderByTimestampDesc(poolId)

        assertEquals(count, result)

    }

    @Test
    fun countBetweenTimestamps() {

        val start = Instant.now().minus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS)
        val end = Instant.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS)

        val pool = SwimmingPool(
            name = "Test Pool",
            vemcount_key = "Key",
            vemcount_stream_id = "1234"
        )

        val poolId = entityManager.persistAndGetId(pool) as Long

        val expected = mutableListOf<SwimmingPoolPeopleCount>()

        (1 until 6).forEach {

            val before = SwimmingPoolPeopleCount(
                poolId = poolId,
                timestamp = start.minus(it * 5L, ChronoUnit.MINUTES),
                peopleCount = 25
            )

            val between = SwimmingPoolPeopleCount(
                poolId = poolId,
                timestamp = start.plus(it * 5L, ChronoUnit.MINUTES),
                peopleCount = 25
            )

            val after = SwimmingPoolPeopleCount(
                poolId = poolId,
                timestamp = end.plus(it * 5L, ChronoUnit.MINUTES),
                peopleCount = 25
            )
            entityManager.persist(before)
            val expectedId = entityManager.persistAndGetId(between) as Long
            expected.add(between.copy(id = expectedId))
            entityManager.persist(after)
        }

        val result = repository.findAllByPoolIdAndTimestampBetween(poolId, start, end)

        assertEquals(expected, result)

    }
}