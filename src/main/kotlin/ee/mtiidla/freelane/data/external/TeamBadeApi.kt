package ee.mtiidla.freelane.data.external

import ee.mtiidla.freelane.data.external.mapper.OpeningHoursApiModelMapper
import ee.mtiidla.freelane.data.external.model.OpeningHoursApiModel
import ee.mtiidla.freelane.data.external.model.PeopleCountApiModel
import ee.mtiidla.freelane.data.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.data.repository.model.SwimmingPoolOpeningHours
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.time.LocalDate

@Component
class TeamBadeApi(
    private val poolRepository: SwimmingPoolRepository,
    private val restClient: RestTemplate
) : PoolInfoApi {

    override fun getPoolPeopleCount(poolId: Long): Int {
        val pool = checkNotNull(poolRepository.findByIdOrNull(poolId))
        val response = restClient.exchange(
            "https://l.vemcount.com/embed/data/${pool.vemcount_key}",
            HttpMethod.GET,
            null,
            PeopleCountApiModel::class.java
        )

        return response.body?.let {
            it.value ?: 0
        } ?: throw IOException("Failed to load people count for pool: $pool")
    }

    override fun getPoolOpeningHours(
        poolId: Long,
        fromDate: LocalDate,
        toDate: LocalDate
    ): List<SwimmingPoolOpeningHours> {
        val pool = checkNotNull(poolRepository.findByIdOrNull(poolId))
        val response = restClient.exchange(
            "https://svoemkbh.kk.dk/opening_hours/instances?" +
                "from_date=$fromDate&to_date=$toDate&nid=${pool.opening_hours_id}",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<OpeningHoursApiModel>>() {})

        val openingHours: List<OpeningHoursApiModel> = response.body
            ?: throw IOException("Failed to load opening hours for pool: $pool")
        return openingHours.map {
            OpeningHoursApiModelMapper.map(it, poolId)
        }
    }
}
