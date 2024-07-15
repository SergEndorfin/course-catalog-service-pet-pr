package click.itkon.dto

import jakarta.validation.constraints.NotBlank

data class InstructorDTO(
    val id: Int?,
    @get:NotBlank(message = "InstructorDTO.name must not be empty")
    val name: String
)
