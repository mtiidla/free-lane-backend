package ee.mtiidla.freelane.controller

import ee.mtiidla.freelane.model.SwimmingPool
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import ee.mtiidla.freelane.service.TeamBadePoolService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SwimmingPoolController(
    private val repository: SwimmingPoolRepository,
    private val poolService: TeamBadePoolService
) {

    @GetMapping("/pools")
    fun getAllSwimmingPools(): List<SwimmingPool> = repository.findAll()

    @GetMapping("/pool/{key}/{id}")
    fun getPoolPeopleCount(
        @PathVariable("key") key: String,
        @PathVariable("id") id: String
    ): Int {
        // TODO: marko 2019-02-03 remove from controller, move to scheduled task
        return poolService.getPoolPeopleCount(key, id)
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