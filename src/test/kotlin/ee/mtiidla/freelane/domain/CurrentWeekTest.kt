package ee.mtiidla.freelane.domain

import ee.mtiidla.freelane.domain.entity.DateRange
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.zone.ZoneRulesException

class CurrentWeekTest {

    @Test
    fun currentWeekAtZone() {

        // now 2019-06-01 Saturday
        val now = ZonedDateTime.of(2019, 6, 1, 12, 0, 0, 0, ZoneId.of("UTC"))

        val expectedMonday = LocalDate.of(2019, 5, 27)
        val expectedSunday = LocalDate.of(2019, 6, 2)
        val expectedDateRange =
            DateRange(expectedMonday, expectedSunday)

        val timeZone = "Europe/Copenhagen"

        val result = CurrentWeek.currentWeekAtZone(timeZone, now)

        assertEquals(expectedDateRange, result)
    }

    @Test
    fun currentWeekAtZone_weekChange() {

        // now 2019-06-02 Sunday at 23:30 UTC is 2019-06-03 Monday at 00/01:30 in Europe/Copenhagen
        val now = ZonedDateTime.of(2019, 6, 2, 23, 30, 0, 0, ZoneId.of("UTC"))

        val expectedMonday = LocalDate.of(2019, 6, 3)
        val expectedSunday = LocalDate.of(2019, 6, 9)
        val expectedDateRange =
            DateRange(expectedMonday, expectedSunday)

        val timeZone = "Europe/Copenhagen"

        val result = CurrentWeek.currentWeekAtZone(timeZone, now)

        assertEquals(expectedDateRange, result)
    }

    @Test
    fun currentWeekAtZone_locale_US_week_start_still_monday() {

        // now 2019-06-01 Saturday
        val now = ZonedDateTime.of(2019, 6, 1, 12, 0, 0, 0, ZoneId.of("UTC"))

        val expectedMonday = LocalDate.of(2019, 5, 27)
        val expectedSunday = LocalDate.of(2019, 6, 2)
        val expectedDateRange =
            DateRange(expectedMonday, expectedSunday)

        val timeZone = "America/New_York"

        val result = CurrentWeek.currentWeekAtZone(timeZone, now)

        assertEquals(expectedDateRange, result)
    }

    @Test(expected = ZoneRulesException::class)
    fun currentWeekAtZone_invalid_timeZone() {

        val timeZone = "Invalid/Zone"
        CurrentWeek.currentWeekAtZone(timeZone)
        fail()
    }
}