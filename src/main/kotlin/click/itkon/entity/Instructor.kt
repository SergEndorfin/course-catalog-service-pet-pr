package click.itkon.entity

import jakarta.persistence.*

@Entity
@Table(name = "instructors")
data class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    var name: String,
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val courses: List<Course> = mutableListOf()
)
