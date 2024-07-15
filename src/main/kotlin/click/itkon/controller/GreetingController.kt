package click.itkon.controller

import click.itkon.service.GreetingService
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/greetings")
class GreetingController(val greetingService: GreetingService) {

    companion object : KLogging()

    @GetMapping("/{name}")
    fun retrieveGreeting(@PathVariable name: String): String {
        logger.info { "> Greeting retrieved: $name" }
        return greetingService.retrieveGreeting(name)
    }
}