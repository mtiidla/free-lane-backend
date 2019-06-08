package ee.mtiidla.freelane.restapi.validation

import java.time.DateTimeException
import java.time.ZoneId
import java.time.zone.ZoneRulesException
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TimeZoneValidator : ConstraintValidator<ValidTimeZone, String> {

    override fun initialize(timeZone: ValidTimeZone) {}

    override fun isValid(
        timeZoneField: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (timeZoneField == null) {
            return true
        } else {
            try {
                ZoneId.of(timeZoneField)
                return true
            } catch (e: DateTimeException) {
                context.buildConstraintViolationWithTemplate(" Invalid time zone format ")
                    .addConstraintViolation()
            } catch (e: ZoneRulesException) {
                context.buildConstraintViolationWithTemplate(" Unknown time zone ")
                    .addConstraintViolation()
            }
        }
        return false
    }
}
