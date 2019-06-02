package ee.mtiidla.freelane.domain

import ee.mtiidla.freelane.domain.entity.DateRange
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.WeekFields
import java.util.Locale

object CurrentWeek {

    /**
     * Returns the current week in the given time zone as a [DateRange] with Monday as the start
     * date of the range and Sunday as the end date of the range.
     *
     * @param timeZone valid time zone String (e.g Europe/Copenhagen), see [ZoneId.of]
     * @throws [java.time.zone.ZoneRulesException] given invalid time zone
     */
    fun currentWeekAtZone(
        timeZone: String,
        now: ZonedDateTime = ZonedDateTime.now()
    ): DateRange {
        val zonedNow = now.withZoneSameInstant(ZoneId.of(timeZone)).toLocalDate()
        val fieldISO = WeekFields.of(Locale.GERMANY).dayOfWeek()
        val monday = zonedNow.with(fieldISO, 1L)
        val sunday = zonedNow.with(fieldISO, 7L)
        return DateRange(monday, sunday)
    }
}