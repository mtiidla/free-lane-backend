package ee.mtiidla.freelane.data.repository

import ee.mtiidla.freelane.data.repository.model.SwimmingPool
import ee.mtiidla.freelane.data.repository.model.SwimmingPoolGroupedPeopleCount
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.time.ZoneOffset

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
            vemcount_key = "Key"
        )

        val poolId = entityManager.persistAndGetId(pool) as Long
        val date = LocalDate.now(ZoneOffset.UTC)

        val count = SwimmingPoolGroupedPeopleCount(
            poolId = poolId,
            date = date,
            timeCount = "12:30:15,10;"
        )

        entityManager.persist(count)

        val result = repository.findByPoolIdAndDate(poolId, date)

        assertEquals(count, result)
    }

    @Test
    fun latestPeopleCountByPoolId() {

        val pool = SwimmingPool(
            name = "Test Pool",
            vemcount_key = "Key"
        )

        val poolId = entityManager.persistAndGetId(pool) as Long

        val yesterdayCount = SwimmingPoolGroupedPeopleCount(
            poolId = poolId,
            date = LocalDate.now().minusDays(1),
            timeCount = "10:28:15,10;"
        )

        entityManager.persist(yesterdayCount)

        val count = SwimmingPoolGroupedPeopleCount(
            poolId = poolId,
            date = LocalDate.now(),
            timeCount = "12:30:45,10;"
        )

        entityManager.persist(count)

        val result = repository.findFirst1ByPoolIdOrderByDateDesc(poolId)

        assertEquals(count, result)
    }

    @Test
    fun countBetweenTimestamps() {

        val start = LocalDate.now().minusDays(1)
        val end = LocalDate.now()

        val pool = SwimmingPool(
            name = "Test Pool",
            vemcount_key = "Key"
        )

        val poolId = entityManager.persistAndGetId(pool) as Long

        val expected = mutableListOf<SwimmingPoolGroupedPeopleCount>()

        val before = SwimmingPoolGroupedPeopleCount(
            poolId = poolId,
            date = start.minusDays(1),
            timeCount = "12:30:45,10;"
        )
        entityManager.persist(before)

        val between1 = SwimmingPoolGroupedPeopleCount(
            poolId = poolId,
            date = start,
            timeCount = "12:30:45,10;"
        )
        val expectedId1 = entityManager.persistAndGetId(between1) as Long
        expected.add(between1.copy(id = expectedId1))

        val between2 = SwimmingPoolGroupedPeopleCount(
            poolId = poolId,
            date = end,
            timeCount = "12:30:45,10;"
        )
        val expectedId2 = entityManager.persistAndGetId(between2) as Long
        expected.add(between2.copy(id = expectedId2))

        val after = SwimmingPoolGroupedPeopleCount(
            poolId = poolId,
            date = end.plusDays(1),
            timeCount = "12:30:45,10;"
        )
        entityManager.persist(after)

        val result = repository.findAllByPoolIdAndDateBetween(poolId, start, end)

        assertEquals(expected, result)
    }
}
