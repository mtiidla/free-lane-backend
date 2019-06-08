package ee.mtiidla.freelane.restapi.validation

import java.time.LocalDate
import java.time.format.DateTimeParseException
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class LocalDateValidator : ConstraintValidator<ValidLocalDate, String> {

    override fun initialize(validLocalDate: ValidLocalDate) {}

    override fun isValid(
        localDateField: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (localDateField == null) {
            return true
        } else {
            try {
                LocalDate.parse(localDateField)
                return true
            } catch (e: DateTimeParseException) {
                context.buildConstraintViolationWithTemplate(
                    "Invalid date format (expected: yyyy-MM-dd)"
                ).addConstraintViolation()
            }
        }
        return false
    }
}
