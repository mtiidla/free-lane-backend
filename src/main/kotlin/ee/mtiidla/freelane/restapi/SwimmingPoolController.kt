package ee.mtiidla.freelane.restapi

import ee.mtiidla.freelane.data.repository.SwimmingPoolOpeningHoursRepository
import ee.mtiidla.freelane.domain.usecase.CreateSwimmingPoolUseCase
import ee.mtiidla.freelane.domain.usecase.DeleteSwimmingPoolUseCase
import ee.mtiidla.freelane.domain.usecase.GetAllSwimmingPoolsUseCase
import ee.mtiidla.freelane.domain.usecase.GetPeopleCountBetweenTimestampsUseCase
import ee.mtiidla.freelane.domain.usecase.UpdateSwimmingPoolUseCase
import ee.mtiidla.freelane.restapi.dto.CreateSwimmingPoolDto
import ee.mtiidla.freelane.restapi.dto.DateRangeDto
import ee.mtiidla.freelane.restapi.dto.UpdateSwimmingPoolDto
import ee.mtiidla.freelane.restapi.viewmodel.CountViewModel
import ee.mtiidla.freelane.restapi.viewmodel.SwimmingPoolViewModel
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class SwimmingPoolController(
    private val openingHoursRepository: SwimmingPoolOpeningHoursRepository,
    private val getAllSwimmingPoolsUseCase: GetAllSwimmingPoolsUseCase,
    private val createSwimmingPoolUseCase: CreateSwimmingPoolUseCase,
    private val updateSwimmingPoolUseCase: UpdateSwimmingPoolUseCase,
    private val deleteSwimmingPoolUseCase: DeleteSwimmingPoolUseCase,
    private val getPeopleCountBetweenTimestampsUseCase: GetPeopleCountBetweenTimestampsUseCase
) {

    @GetMapping("/pools")
    fun getAllSwimmingPools(): List<SwimmingPoolViewModel> {
        return getAllSwimmingPoolsUseCase.execute()
    }

    @GetMapping("/pools/{id}/counts")
    fun getSwimmingPoolCountsBetweenTimestamps(
        @PathVariable("id") poolId: Long,
        @Valid dateRangeDto: DateRangeDto
    ): List<CountViewModel> {
        return getPeopleCountBetweenTimestampsUseCase.execute(
            GetPeopleCountBetweenTimestampsUseCase.Request(
                poolId,
                LocalDate.parse(dateRangeDto.start_date),
                LocalDate.parse(dateRangeDto.end_date)
            )
        )
    }

    @PostMapping("/pools")
    fun createSwimmingPool(@Valid swimmingPool: CreateSwimmingPoolDto): SwimmingPoolViewModel {
        return createSwimmingPoolUseCase.execute(CreateSwimmingPoolUseCase.Request(swimmingPool))
    }

    @PatchMapping("/pools/{id}")
    fun updateSwimmingPool(
        @PathVariable("id") id: Long,
        @Valid updateSwimmingPoolDto: UpdateSwimmingPoolDto
    ): SwimmingPoolViewModel {
        return updateSwimmingPoolUseCase.execute(
            UpdateSwimmingPoolUseCase.Request(id, updateSwimmingPoolDto)
        )
    }

    @DeleteMapping("/pools/{id}")
    fun deleteSwimmingPool(@PathVariable("id") id: Long) {
        deleteSwimmingPoolUseCase.execute(DeleteSwimmingPoolUseCase.Request(id))
    }
}
