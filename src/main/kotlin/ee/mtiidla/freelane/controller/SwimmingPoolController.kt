package ee.mtiidla.freelane.controller

import ee.mtiidla.freelane.model.SwimmingPool
import ee.mtiidla.freelane.model.SwimmingPoolOpeningHours
import ee.mtiidla.freelane.model.SwimmingPoolPeopleCount
import ee.mtiidla.freelane.repository.SwimmingPoolOpeningHoursRepository
import ee.mtiidla.freelane.repository.SwimmingPoolPeopleCountRepository
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.service.TeamBadePoolService
import ee.mtiidla.freelane.viewmodel.CountViewModel
import ee.mtiidla.freelane.viewmodel.OpeningHoursViewModel
import ee.mtiidla.freelane.viewmodel.SwimmingPoolViewModel
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/api")
class SwimmingPoolController(
    private val repository: SwimmingPoolRepository,
    private val countRepository: SwimmingPoolPeopleCountRepository,
    private val openingHoursRepository: SwimmingPoolOpeningHoursRepository,
    private val poolService: TeamBadePoolService
) {

    @GetMapping("/pools")
    fun getAllSwimmingPools(): List<SwimmingPoolViewModel> {
        return repository.findAll()
            .map { pool ->
                val hours = openingHoursRepository.findAllByPoolId(pool.id)
                    .map { OpeningHoursViewModel(it.dayOfWeek, it.open, it.closed) }
                val count = countRepository.findFirst1ByPoolIdOrderByTimestampDesc(pool.id)
                SwimmingPoolViewModel(
                    pool.id,
                    pool.name,
                    pool.url,
                    CountViewModel(count.timestamp, count.peopleCount),
                    hours
                )
            }
    }

    @GetMapping("/pools/{id}/counts")
    fun getSwimmingPoolCountsBetweenTimestamps(
        @PathVariable("id") poolId: Long,
        @RequestParam("start_date") start: String,
        @RequestParam("end_date") end: String
    ): List<CountViewModel> {
        val openingHours = openingHoursRepository.findAllByPoolId(poolId)
        var startDate = LocalDate.parse(start)
        val endDate = LocalDate.parse(end)
        val allCounts = mutableListOf<SwimmingPoolPeopleCount>()

        while (!startDate.isAfter(endDate)) {
            val dayOfWeek = startDate.dayOfWeek.value
            val openingHour = checkNotNull(openingHours.firstOrNull { it.dayOfWeek == dayOfWeek })
            val open = LocalTime.parse(openingHour.open)
            val closed = LocalTime.parse(openingHour.closed)
            // TODO: marko 2019-02-23 convert start date time to pool timezone
            val queryStart = startDate.atTime(open).toInstant(ZoneOffset.UTC)
            val queryEnd = startDate.atTime(closed).toInstant(ZoneOffset.UTC)

            val counts =
                countRepository.findAllByPoolIdAndTimestampBetween(poolId, queryStart, queryEnd)
            allCounts.addAll(counts)

            startDate = startDate.plusDays(1)
        }

        return allCounts.map {
            CountViewModel(it.timestamp, it.peopleCount)
        }
    }

    @GetMapping("/pool/{key}/{id}")
    fun getPoolPeopleCount(
        @PathVariable("key") key: String,
        @PathVariable("id") id: String
    ): Int {
        // TODO: marko 2019-02-03 remove from controller, move to scheduled task
        return poolService.getPoolPeopleCount(key, id)
    }

    @PostMapping("/pools/{id}/opening_hours")
    fun createOpeningHourForPool(
        @PathVariable("id") poolId: Long,
        @RequestParam("day") day: Int,
        @RequestParam("open") open: String,
        @RequestParam("closed") closed: String
    ): OpeningHoursViewModel {
        val hours = openingHoursRepository.save(
            SwimmingPoolOpeningHours(
                poolId = poolId,
                dayOfWeek = day,
                open = open,
                closed = closed
            )
        )
        return OpeningHoursViewModel(hours.dayOfWeek, hours.open, hours.closed)
    }

    @PostMapping("/pools")
    fun createSwimmingPool(
        @RequestParam("name") name: String,
        @RequestParam("url") url: String,
        @RequestParam("vemcount_key") key: String,
        @RequestParam("vemcount_stream_id") streamId: String
    ): SwimmingPool {
        return repository.save(
            SwimmingPool(
                name = name,
                url = url,
                vemcount_key = key,
                vemcount_stream_id = streamId
            )
        )
    }

    @DeleteMapping("/pools/{id}")
    fun deleteSwimmingPool(@PathVariable("id") id: Long) {
        repository.deleteById(id)
    }
}