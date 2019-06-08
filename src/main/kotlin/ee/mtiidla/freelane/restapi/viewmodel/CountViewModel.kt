package ee.mtiidla.freelane.restapi.viewmodel

import java.time.Instant

data class CountViewModel(
    val timestamp: Instant,
    val people: Int
)
