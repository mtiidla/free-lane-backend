package ee.mtiidla.freelane.data.repository

import ee.mtiidla.freelane.data.repository.model.SwimmingPool
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class SwimmingPoolRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var repository: SwimmingPoolRepository

    @Test
    fun savesPool() {

        val pool = SwimmingPool(
            name = "Test Pool",
            url = "https://svoemkbh.kk.dk/sundby-bad",
            address = "Sundbyvestervej 50, 2300 København S",
            latitude = 55.646458F,
            longitude = 12.599678F,
            vemcount_key = "Key",
            vemcount_stream_id = "1234",
            opening_hours_id = 1305,
            time_zone = "Europe/Copenhagen"
        )

        entityManager.persist(pool)
        entityManager.flush()

        val result = repository.getOne(1)

        assertEquals(pool, result)
    }
}