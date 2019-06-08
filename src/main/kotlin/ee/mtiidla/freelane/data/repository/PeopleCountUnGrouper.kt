package ee.mtiidla.freelane.data.repository

import ee.mtiidla.freelane.data.repository.model.SwimmingPoolGroupedPeopleCount
import ee.mtiidla.freelane.data.repository.model.SwimmingPoolPeopleCount
import org.springframework.stereotype.Component
import java.time.LocalTime
import java.time.ZoneOffset

@Component
class PeopleCountUnGrouper {

    fun ungroup(grouped: SwimmingPoolGroupedPeopleCount): List<SwimmingPoolPeopleCount> {
        val timestampCounts = grouped.timeCount.split(";")
        return timestampCounts
            .asSequence()
            .filterNot { it.isBlank() }
            .map {
                val (timestamp, count) = it.split(",")
                val dateTime =
                    grouped.date.atTime(LocalTime.parse(timestamp)).atZone(ZoneOffset.UTC)
                SwimmingPoolPeopleCount(grouped.poolId, dateTime.toInstant(), count.toInt())
            }.toList()
    }

    fun ungroupFirst(grouped: SwimmingPoolGroupedPeopleCount) : SwimmingPoolPeopleCount? {
        // TODO: marko 2019-04-07 could optimize for first/last count not parse full string
        return ungroup(grouped).firstOrNull()
    }
}
