package ee.mtiidla.freelane.repository

import ee.mtiidla.freelane.model.SwimmingPool
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
            vemcount_key = "Key",
            vemcount_stream_id = "1234"
        )

        entityManager.persist(pool)
        entityManager.flush()

        val result = repository.getOne(1)

        assertEquals(pool, result)

    }
}