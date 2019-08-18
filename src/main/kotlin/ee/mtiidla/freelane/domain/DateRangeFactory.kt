package ee.mtiidla.freelane.domain

import ee.mtiidla.freelane.domain.entity.DateRange
import java.time.ZoneId
import java.time.ZonedDateTime

object DateRangeFactory {

    /**
     * Returns the running 7 days in the given time zone as a [DateRange] with current day as the
     * start of the range.
     *
     * @param timeZone valid time zone String (e.g Europe/Copenhagen), see [ZoneId.of]
     * @throws [java.time.zone.ZoneRulesException] given invalid time zone
     */
    fun running7DaysAtZone(
        timeZone: String,
        now: ZonedDateTime = ZonedDateTime.now()
    ): DateRange {
        val zonedNow = now.withZoneSameInstant(ZoneId.of(timeZone)).toLocalDate()
        return DateRange(zonedNow, zonedNow.plusDays(6))
    }
}
