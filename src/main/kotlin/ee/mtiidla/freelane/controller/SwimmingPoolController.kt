package ee.mtiidla.freelane.controller

import ee.mtiidla.freelane.model.SwimmingPool
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.service.TeamBadePoolService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SwimmingPoolController(
    private val repository: SwimmingPoolRepository,
    private val poolService: TeamBadePoolService
) {

    @GetMapping("/pools")
    fun getAllPools(): List<SwimmingPool> =
        repository.findAll()

    @GetMapping("/pool/{key}/{id}")
    fun getPoolPeopleCount(
        @PathVariable("key") key: String,
        @PathVariable("id") id: Long
    ): Int {
        // TODO: marko 2019-02-03 remove from controller, move to scheduled task
        return poolService.getPoolPeopleCount(key, id)
    }
}