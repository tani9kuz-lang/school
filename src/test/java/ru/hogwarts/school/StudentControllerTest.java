package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private FacultyRepository facultyRepository;


    @BeforeEach
    void setUp() {

        avatarRepository.deleteAllInBatch();
        studentRepository.deleteAllInBatch();
        facultyRepository.deleteAllInBatch();
    }

    // Создание студента
    @Test
    void shouldCreateStudent() {
        Student student = new Student(null, "Иван Иванов", 11);
        String url = "http://localhost:" + port + "/student";

        ResponseEntity<Student> response = restTemplate.postForEntity(url, student, Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Иван Иванов");
    }

    // Получение студента по ID
    @Test
    void shouldGetStudentById() {
        Student savedStudent = studentRepository.save(new Student(null, "Петров Петр", 11));
        String url = "http://localhost:" + port + "/student/" + savedStudent.getId();

        ResponseEntity<Student> response = restTemplate.getForEntity(url, Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Петров Петр");
    }

    // Изменение данных
    @Test
    void shouldEditStudent() {
        Student savedStudent = studentRepository.save(new Student(null, "Никита Никитин", 11));
        savedStudent.setName("Никита Иванов");
        String url = "http://localhost:" + port + "/student";

        HttpEntity<Student> entity = new HttpEntity<>(savedStudent);
        ResponseEntity<Student> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        Assertions.assertThat(response.getBody().getName()).isEqualTo("Никита Иванов");
    }

    // Удаление студента
    @Test
    void shouldDeleteStudent() {
        Student savedStudent = studentRepository.save(new Student(null, "Александр Петров", 12));
        String url = "http://localhost:" + port + "/student/" + savedStudent.getId();

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(studentRepository.findById(savedStudent.getId())).isEmpty();
    }

    // Фильтрация по конкретному возрасту
    @Test
    void shouldGetStudentsByAge() {
        studentRepository.save(new Student(null, "Иван", 11));
        studentRepository.save(new Student(null, "Петр", 11));
        studentRepository.save(new Student(null, "Александр", 17));

        String url = "http://localhost:" + port + "/student/filter?age=11";
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().size()).isEqualTo(2);
    }

    // Поиск по диапазону возраста
    @Test
    void shouldGetStudentsByAgeBetween() {
        studentRepository.save(new Student(null, "Петр", 12));
        studentRepository.save(new Student(null, "Никита", 13));
        studentRepository.save(new Student(null, "Иван", 15));
        String url = "http://localhost:" + port + "/student/age-range?min=12&max=14";

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().size()).isEqualTo(2);
    }
}
