package ee.mtiidla.freelane

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Application

private val log = LoggerFactory.getLogger(Application::class.java)

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
    log.info("Hello world")
}