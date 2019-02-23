package ee.mtiidla.freelane

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate

@EnableScheduling
@SpringBootApplication
open class Application {

    @Bean
    open fun jacksonKotlinModule(): Module {
        return KotlinModule()
    }

    @Bean
    open fun restClient(): RestTemplate {
        return RestTemplate()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
