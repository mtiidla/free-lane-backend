package ee.mtiidla.freelane.controller

import ee.mtiidla.freelane.model.SwimmingPool
import ee.mtiidla.freelane.model.SwimmingPoolGroupedPeopleCount
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
import java.time.Instant
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
                    .map {
                        OpeningHoursViewModel(
                            it.dayOfWeek,
                            it.open.toString(),
                            it.closed.toString()
                        )
                    }
                val peopleCount = countRepository.findFirst1ByPoolIdOrderByDateDesc(pool.id)
                val count = peopleCount?.let {
                    // TODO: marko 2019-04-07 could optimize for latest count not parse full string
                    val latestCount = ungroupCount(it).last()
                    CountViewModel(latestCount.timestamp, latestCount.peopleCount)

                } ?: CountViewModel(Instant.now(), 0)
                SwimmingPoolViewModel(
                    pool.id,
                    pool.name,
                    pool.url,
                    count,
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
        var startDate = LocalDate.parse(start)
        val endDate = LocalDate.parse(end)

        val openingHours = openingHoursRepository.findAllByPoolId(poolId)
        val allCounts = mutableListOf<SwimmingPoolPeopleCount>()

        while (!startDate.isAfter(endDate)) {
            val queryStart = startDate
            val queryEnd = endDate

            val groupedCount =
                countRepository.findAllByPoolIdAndDateBetween(poolId, queryStart, queryEnd)

            val dayOfWeek = startDate.dayOfWeek.value
            val openingHour = checkNotNull(openingHours.firstOrNull { it.dayOfWeek == dayOfWeek })
            // TODO: marko 2019-02-23 convert start date time to pool timezone?
            val filterStart = startDate.atTime(openingHour.open).toInstant(ZoneOffset.UTC)
            val filterEnd = startDate.atTime(openingHour.closed).toInstant(ZoneOffset.UTC)

            allCounts.addAll(groupedCount.flatMap(this::ungroupCount)
                .filter { filterEnd.isAfter(it.timestamp) && filterStart.isBefore(it.timestamp) })

            startDate = startDate.plusDays(1)
        }

        return allCounts.map {
            CountViewModel(it.timestamp, it.peopleCount)
        }
    }

    // TODO: marko 2019-03-10 refactor into repo etc
    private fun ungroupCount(grouped: SwimmingPoolGroupedPeopleCount): List<SwimmingPoolPeopleCount> {
        val timestampCounts = grouped.timeCount.split(";")
        return timestampCounts
            .asSequence()
            .filterNot { it.isBlank() }
            .map {
                val (timestamp, count) = it.split(",")
                val dateTime = grouped.date.atTime(LocalTime.parse(timestamp)).atZone(ZoneOffset.UTC)
                SwimmingPoolPeopleCount(grouped.poolId, dateTime.toInstant(), count.toInt())
            }.toList()
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
                open = LocalTime.parse(open),
                closed = LocalTime.parse(closed)
            )
        )
        return OpeningHoursViewModel(
            hours.dayOfWeek,
            hours.open.toString(),
            hours.closed.toString()
        )
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