package ee.mtiidla.freelane.domain

import ee.mtiidla.freelane.domain.entity.DateRange
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.zone.ZoneRulesException

class DateRangeFactoryTest {

    @Test
    fun running7DaysAtZone() {

        // now 2019-06-01 Saturday
        val now = ZonedDateTime.of(2019, 6, 1, 12, 0, 0, 0, ZoneId.of("UTC"))

        val expectedStart = LocalDate.of(2019, 6, 1)
        val expectedEnd = LocalDate.of(2019, 6, 7)
        val expectedDateRange =
            DateRange(expectedStart, expectedEnd)

        val timeZone = "Europe/Copenhagen"

        val result = DateRangeFactory.running7DaysAtZone(timeZone, now)

        assertEquals(expectedDateRange, result)
    }

    @Test
    fun running7DaysAtZone_timeZoneDayChange() {

        // now 2019-06-02 Sunday at 23:30 UTC is 2019-06-03 Monday at 00/01:30 in Europe/Copenhagen
        val now = ZonedDateTime.of(2019, 6, 2, 23, 30, 0, 0, ZoneId.of("UTC"))

        val expectedStart = LocalDate.of(2019, 6, 3)
        val expectedEnd = LocalDate.of(2019, 6, 9)
        val expectedDateRange =
            DateRange(expectedStart, expectedEnd)

        val timeZone = "Europe/Copenhagen"

        val result = DateRangeFactory.running7DaysAtZone(timeZone, now)

        assertEquals(expectedDateRange, result)
    }

    @Test(expected = ZoneRulesException::class)
    fun running7DaysAtZone_invalid_timeZone() {

        val timeZone = "Invalid/Zone"
        DateRangeFactory.running7DaysAtZone(timeZone)
        fail()
    }
}
