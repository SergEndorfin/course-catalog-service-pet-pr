package click.itkon.controller

import click.itkon.dto.InstructorDTO
import click.itkon.service.InstructorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/instructors")
@Validated
class InstructorController(
    private val instructorService: InstructorService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor(@Valid @RequestBody instructorDTO: InstructorDTO): InstructorDTO =
        instructorService.createInstructor(instructorDTO)
}
