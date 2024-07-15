package click.itkon.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GreetingService {

    @Value("\${message}")
    lateinit var message: String

    fun retrieveGreeting(name: String) = "Hello, $name! And Profile is: $message"

}
