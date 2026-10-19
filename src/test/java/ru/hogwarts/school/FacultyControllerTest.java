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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @BeforeEach
    void setUp() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    // Создание факультета
    @Test
    void shouldCreateFaculty() {
        Faculty faculty = new Faculty(null, "Гриффиндор", "Красный");
        String url = "http://localhost:" + port + "/faculty";

        ResponseEntity<Faculty> response = restTemplate.postForEntity(url, faculty, Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Гриффиндор");
    }


    //Получение факультета по ID
    @Test
    void shouldGetFaculty() {
        Faculty savedFaculty = facultyRepository.save(new Faculty(null, "Гриффиндор", "Крассный"));
        String url = "http://localhost:" + port + "/faculty/" + savedFaculty.getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(url, Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Гриффиндор");
    }

    //Редактирование факультета
    @Test
    void shouldEditFaculty() {
        Faculty saveFaculty = facultyRepository.save(new Faculty(null, "Гриффиндор", "Крассный"));
        saveFaculty.setName("Слизерин");
        String url = "http://localhost:" + port + "/faculty";

        HttpEntity<Faculty> entity = new HttpEntity<>(saveFaculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Слизерин");
    }

    // Удаление факультета
    @Test
    void shouldDeleteFaculty() {
        Faculty saveFaculty = facultyRepository.save(new Faculty(null, "Слизерин", "Слизерин"));
        String url = "http://localhost:" + port + "/faculty/" + saveFaculty.getId();
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(facultyRepository.findById(saveFaculty.getId())).isEmpty();
    }

    // Поиск факультета по цвету
    @Test
    void shouldGetFacultiesByColor() {
        facultyRepository.save(new Faculty(null, "Гриффиндор", "Красный"));
        facultyRepository.save(new Faculty(null, "Слизерин", "Зеленый"));
        String url = "http://localhost:" + port + "/faculty/filter?color=Красный";
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().size()).isEqualTo(1);
    }

    // Получение факультета по имени или  цвету
    @Test
    void shouldGetFacultiesByNameOrColor() {
        facultyRepository.save(new Faculty(null, "Гриффиндор", "Красный"));
        facultyRepository.save(new Faculty(null, "Слизерин", "Зеленый"));
        String urlByName = "http://localhost:" + port + "/faculty/search?param=ГРИФФИНДОР";

        ResponseEntity<Collection<Faculty>> responseByName = restTemplate.exchange(
                urlByName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );

        Assertions.assertThat(responseByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseByName.getBody()).isNotNull();
        Assertions.assertThat(responseByName.getBody().iterator().next().getName()).isEqualTo("Гриффиндор");
    }

    // Получение студента по факультету
    @Test
    void shouldGetStudentsByFaculty() {
        Faculty griffin = facultyRepository.save(new Faculty(null, "Гриффиндор", "Красный"));

        Student ivan = new Student(null, "Иван Иванов", 11);
        ivan.setFaculty(griffin);

        Student petr = new Student(null, "Петр Петров", 11);
        petr.setFaculty(griffin);

        studentRepository.save(ivan);
        studentRepository.save(petr);

        String url = "http://localhost:" + port + "/faculty/" + griffin.getId() + "/students";

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().size()).isEqualTo(2);
    }

}