package ee.mtiidla.freelane.service

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.io.IOException

@Component
class TeamBadePoolService(private val restClient: RestTemplate) {

    fun getPoolPeopleCount(vemcountKey: String, vemcountId: String): Int {
        val response = restClient.exchange(
            "https://l.vemcount.com/w/$vemcountKey/stream?data[]=$vemcountId",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<PoolPeopleCount>>() {})
        return response.body?.firstOrNull { it.value != null }?.value
            ?: throw IOException("Failed to load people count for key:$vemcountKey, id: $vemcountId")
    }

    data class PoolPeopleCount(val value: Int?)
}