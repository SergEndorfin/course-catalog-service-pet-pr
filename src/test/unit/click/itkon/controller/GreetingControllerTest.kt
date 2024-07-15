package click.itkon.controller

import click.itkon.service.GreetingService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [GreetingController::class])
@AutoConfigureWebTestClient
class GreetingControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient
    @MockkBean
    lateinit var greetingServiceMock: GreetingService

    @Test
    fun retrieveGreeting() {
        val messageFromPropsFile = "DEFAULT profile here !!"
        val name = "test data"
        val expectedResponse = "Hello, $name! And Profile is: $messageFromPropsFile"

        every { greetingServiceMock.retrieveGreeting(any()) } returns expectedResponse

        val result = webTestClient.get()
            .uri("/v1/greetings/{name}", name)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(String::class.java)
            .returnResult()

        assertEquals(expectedResponse, result.responseBody)
    }
}
