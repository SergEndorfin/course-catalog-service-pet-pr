package click.itkon.util

import click.itkon.dto.CourseDTO
import click.itkon.entity.Course
import click.itkon.entity.Instructor

fun courseEntityList() = listOf(
    Course(null, "Build RestFul APis using SpringBoot and Kotlin", "Development"),
    Course(null, "Build Reactive Microservices using Spring WebFlux/SpringBoot", "Development"),
    Course(null, "Wiremock for Java Developers", "Development")
)

fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APis using Spring Boot and Kotlin",
    category: String = "Dilip Sundarraj",
//    instructorId: Int? = 1
) = CourseDTO(
    id,
    name,
    category,
//    instructorId
)

fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(
        null,
        "Build RestFul APis using SpringBoot and Kotlin", "Development",
        instructor
    ),
    Course(
        null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot", "Development", instructor
    ),
    Course(
        null,
        "Wiremock for Java Developers", "Development",
        instructor
    )
)

fun instructorEntity(name: String = "test data") = Instructor(null, name)
