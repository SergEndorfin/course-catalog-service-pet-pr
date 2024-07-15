package click.itkon.service

import click.itkon.dto.CourseDTO
import click.itkon.entity.Course
import click.itkon.exception.CourseNotFoundException
import click.itkon.exception.InstructorNotValidException
import click.itkon.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService
) {

    companion object : KLogging()

    fun createCourse(courseDTO: CourseDTO): CourseDTO {
        val instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId!!)

        if (instructorOptional.isEmpty) {
            throw InstructorNotValidException("InstructorId: ${courseDTO.instructorId} not valid ")
        }

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructorOptional.get())
        }
        courseRepository.save(courseEntity)

        logger.info { "Created new course: $courseEntity" }

        return courseEntity
            .let { CourseDTO(it.id, it.name, it.category, it.instructor!!.id) }
    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {
        return (courseName?.let { courseRepository.findCoursesByName(courseName) }
            ?: courseRepository.findAll()).map { CourseDTO(it.id, it.name, it.category) }
    }

    fun updateCourse(id: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRepository.findById(id)
        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name, it.category)
                }
        } else {
            throw CourseNotFoundException("Course with Id: $id not found")
        }
    }

    fun deleteCourse(id: Int) {
        courseRepository.findById(id)
            .ifPresentOrElse(
                { courseRepository.deleteById(id) },
                { throw CourseNotFoundException("Course with Id: $id not found") })
    }
}
