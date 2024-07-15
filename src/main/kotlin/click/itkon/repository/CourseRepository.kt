package click.itkon.repository

import click.itkon.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository


interface CourseRepository : CrudRepository<Course, Int> {

    fun findByNameContaining(name: String): List<Course>

    @Query(value = "select c from Course c where c.name like %?1%")
    fun findCoursesByName(name: String): List<Course>
}
