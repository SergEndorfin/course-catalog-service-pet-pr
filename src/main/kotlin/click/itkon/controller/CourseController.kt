package click.itkon.controller

import click.itkon.dto.CourseDTO
import click.itkon.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(val courseService: CourseService) {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createCourse(@Valid @RequestBody courseDTO: CourseDTO): CourseDTO {
        return courseService.createCourse(courseDTO)
    }

    @GetMapping()
    fun retrieveAllCourse(@RequestParam ("course_name", required = false) courseName : String?): List<CourseDTO> {
        return courseService.retrieveAllCourses(courseName)
    }

    @PutMapping("/{course_id}")
    fun updateCourse(@PathVariable("course_id") id: Int, @RequestBody courseDTO: CourseDTO): CourseDTO {
        return courseService.updateCourse(id, courseDTO)
    }

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") id: Int) = courseService.deleteCourse(id)
}
