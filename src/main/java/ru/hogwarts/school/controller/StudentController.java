package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RequestMapping("student")
@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createStudent = studentService.createStudent(student);
        Student createdStudent;
        return ResponseEntity.ok(createStudent);
    }

    @GetMapping("{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable long studentId) {
        Student student = studentService.findStudent(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.findStudent(student.getId());
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        Student updatedStudent = studentService.editStudent(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/filter")
    public ResponseEntity<Collection<Student>> getStudentsByAge(@RequestParam("age") int age) {
        Collection<Student> result = studentService.findStudentsByAge(age);

        return ResponseEntity.ok(result);
    }

}
