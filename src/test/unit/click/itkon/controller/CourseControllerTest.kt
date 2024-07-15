package click.itkon.controller

import click.itkon.dto.CourseDTO
import click.itkon.service.CourseService
import click.itkon.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMockk: CourseService

    @Test
    fun createCourse() {
        val courseDTO = CourseDTO(null, "Title", "Category", 1)

        every { courseServiceMockk.createCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertNotNull(savedCourseDTO!!.id)
    }

    @Test
    fun retrieveAllCourse() {
        every { courseServiceMockk.retrieveAllCourses(null) }.returnsMany(listOf(courseDTO(id = 2), courseDTO(id = 3)))
        val courseDTOs = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        println("courseDTOs:\n$courseDTOs")
        assertEquals(2, courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {
        val courseDTO = courseDTO(id = 100)

        every { courseServiceMockk.updateCourse(any(), any()) } returns courseDTO

        val updatedCourse = webTestClient.put()
            .uri("/v1/courses/{course_id}", 100)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Build RestFul APis using Spring Boot and Kotlin", updatedCourse!!.name)
    }

    @Test
    fun deleteCourse() {
        every { courseServiceMockk.deleteCourse(any()) } just runs

        webTestClient.delete()
            .uri("/v1/courses/{course_id}", 11)
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun createCourse_validation() {
        val courseDTO = CourseDTO(null, "", "", 1)

        every { courseServiceMockk.createCourse(any()) } returns courseDTO(id = 1)

        val responseBody = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            "courseDTO.category must not be blank, courseDTO.name must not be blank",
            responseBody
        )
    }

    @Test
    fun createCourse_runtimeException() {
        val courseDTO = CourseDTO(null, "Test", "Category", 1)

        val errorMsg = "Unexpected error occur"
        every { courseServiceMockk.createCourse(any()) } throws RuntimeException(errorMsg)

        val responseBody = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMsg, responseBody)
    }
}
