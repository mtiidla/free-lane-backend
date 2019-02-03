package ee.mtiidla.freelane.controller

import ee.mtiidla.freelane.model.SwimmingPool
import ee.mtiidla.freelane.repository.SwimmingPoolRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SwimmingPoolController(private val repository: SwimmingPoolRepository) {

    @GetMapping("/pools")
    fun getAllPools(): List<SwimmingPool> =
        repository.findAll()

}