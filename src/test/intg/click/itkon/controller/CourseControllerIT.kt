package click.itkon.controller

import click.itkon.dto.CourseDTO
import click.itkon.entity.Course
import click.itkon.repository.CourseRepository
import click.itkon.repository.InstructorRepository
import click.itkon.util.PostgreSQLContainerInitializer
import click.itkon.util.courseEntityList
import click.itkon.util.instructorEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIT : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun createCourse() {
        val instructor = instructorRepository.findAll().first()
        val courseDTO = CourseDTO(null, "Title", "Category", instructor.id)

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
        val courseDTOs = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        println("courseDTOs:\n$courseDTOs")
        assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun retrieveAllCourseByName() {
        val uri = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("course_name", "SpringBoot")
            .toUriString()

        val courseDTOs = webTestClient.get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(2, courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {
        val instructor = instructorRepository.findAll().first()
        val course = Course(null, "Build RestFul APis using SpringBoot and Kotlin", "Development", instructor)
        courseRepository.save(course)
        val updatedCourseDTO = CourseDTO(null, "Build RestFul APis", "Development")

        val updatedCourse = webTestClient.put()
            .uri("/v1/courses/{course_id}", course.id)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Build RestFul APis", updatedCourse!!.name)
    }

    @Test
    fun deleteCourse() {
        val instructor = instructorRepository.findAll().first()
        val course = Course(null, "Build RestFul APis using SpringBoot and Kotlin", "Development", instructor)
        courseRepository.save(course)

        webTestClient.delete()
            .uri("/v1/courses/{course_id}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}
