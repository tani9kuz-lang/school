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
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc; // Главный инструмент симуляции HTTP-запросов

    @MockitoBean
    private StudentService studentService; // Создаем мок-заглушку сервиса студентов

    @Autowired
    private ObjectMapper objectMapper;

    //Создание студента
    @Test
    void shouldCreateStudent() throws Exception{
        Student student = new Student(1L, "Иван Иванов", 11);
        Mockito.when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Иван Иванов"))
                .andExpect(jsonPath("$.age").value(11));
    }

    //  получение студента по ID
    @Test
    void shouldGetStudentById() throws Exception {
        Student student = new Student(1L, "Петров Петр", 11);

        Mockito.when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Петров Петр"));
    }

    // изменение данных студента
    @Test
    void shouldEditStudent() throws Exception {
        Student student = new Student(1L, "Никита Иванов", 11);

        Mockito.when(studentService.findStudent(1L)).thenReturn(student);
        Mockito.when(studentService.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Никита Иванов"));
    }

    // удаления студента
    @Test
    void shouldDeleteStudent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1"))
                .andExpect(status().isOk());
        Mockito.verify(studentService, Mockito.times(1)).deleteStudent(1L);
    }

    // фильтрация по конкретному возрасту
    @Test
    void shouldGetStudentsByAge() throws Exception {
        List<Student> students = List.of(new Student(1L, "Иван", 11));

        Mockito.when(studentService.findStudentsByAge(11)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/filter?age=11")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Иван"));
    }

    // поиск студентов по диапазону возраста
    @Test
    void shouldGetStudentsByAgeBetween() throws Exception {
        List<Student> students = List.of(new Student(1L, "Петр", 12));

        Mockito.when(studentService.findByAgeBetween(12, 14)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age-range?min=12&max=14")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Петр"));
    }

}

