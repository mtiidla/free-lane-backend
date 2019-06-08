package ee.mtiidla.freelane.restapi.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Suppress("unused") // required method contract for validation api
@Constraint(validatedBy = [LocalDateValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidLocalDate(
    val message: String = "Invalid date",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
)
