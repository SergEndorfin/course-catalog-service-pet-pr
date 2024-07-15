package click.itkon.service

import click.itkon.dto.InstructorDTO
import click.itkon.entity.Instructor
import click.itkon.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(private val instructorRepository: InstructorRepository) {

    fun createInstructor(instructorDto: InstructorDTO): InstructorDTO {
        return instructorRepository.save(
            instructorDto.let { Instructor(it.id, it.name) }
        ).let { InstructorDTO(it.id, it.name) }
    }

    fun findByInstructorId(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }
}
