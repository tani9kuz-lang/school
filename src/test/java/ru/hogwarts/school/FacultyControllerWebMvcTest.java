package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    // Создание факультета
    @Test
    void shouldCreateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Гриффиндор", "Красный");

        Mockito.when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"));
    }

    // Получение факультета по ID
    @Test
    void shouldGetFacultyById() throws Exception {
        Faculty faculty = new Faculty(1L, "Слизерин", "Зеленый");

        Mockito.when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Зеленый"));
    }

    // Редактирование факультета
    @Test
    void shouldEditFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Когтевран", "Бронзовый");

        Mockito.when(facultyService.findFaculty(1L)).thenReturn(faculty);
        Mockito.when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Бронзовый"));
    }

    // Удаление факультета
    @Test
    void shouldDeleteFaculty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1"))
                .andExpect(status().isOk());

        Mockito.verify(facultyService, Mockito.times(1)).deleteFaculty(1L);
    }

    // Поиск факультета по цвету
    @Test
    void shouldGetFacultiesByColor() throws Exception {
        List<Faculty> faculties = List.of(new Faculty(1L, "Гриффиндор", "Красный"));

        Mockito.when(facultyService.findFacultyByColor("Красный")).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter?color=Красный")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"));
    }

    // Получение факультета по имени или цвету
    @Test
    void shouldGetFacultiesByNameOrColorSearch() throws Exception {
        List<Faculty> faculties = List.of(new Faculty(1L, "Слизерин", "Зеленый"));

        Mockito.when(facultyService.findFacultyByNameOrColor("СЛИЗЕРИН")).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/search?param=СЛИЗЕРИН")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // ИСПРАВЛЕНО: проверяем имя у ПЕРВОГО элемента массива ($[0])
                .andExpect(jsonPath("$[0].name").value("Слизерин"));
    }


    // Получение студента по факультету
    @Test
    void shouldGetStudentsByFaculty() throws Exception {
        List<Student> students = List.of(new Student(1L, "Иван", 11), new Student(2L, "Петр", 11));
        Faculty faculty = new Faculty(1L, "Гриффиндор", "Красный");
        faculty.setStudents(students);

        Mockito.when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Иван"));
    }
}