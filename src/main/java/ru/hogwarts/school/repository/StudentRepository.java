package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);
    Collection<Student> findByAgeBetween(int min, int max);

    //общее количество студентов
    @Query(value = "SELECT COUNT(s) FROM Student s")
    Integer getStudentsCount();

    // средний возраст студентов
    @Query(value = "SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    //  5 последних добавленных студентов
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudents();
}
