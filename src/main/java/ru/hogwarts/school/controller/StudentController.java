package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RequestMapping("student")
@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable Long studentId) {
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

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<Collection<Student>> getStudentsByAge(@RequestParam("age") int age) {
        Collection<Student> result = studentService.findStudentsByAge(age);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/age-range")
    public ResponseEntity<Collection<Student>> getStudentsByAgeBetween(@RequestParam("min") int min,
                                                                       @RequestParam("max") int max) {
        Collection<Student> result = studentService.findByAgeBetween(min, max);
        return ResponseEntity.ok(result);
    }


    @GetMapping("{studentId}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudent(@PathVariable Long studentId) {
        Student student = studentService.findStudent(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getFaculty());
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getStudentsCount() {
        Integer count = studentService.getStudentsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        Double averageAge = studentService.getAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/last-five")
    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        Collection<Student> lastFive = studentService.getLastFiveStudents();
        return ResponseEntity.ok(lastFive);
    }

    @GetMapping("/all-student")
    public ResponseEntity<List<String>> getAllStudentsNamesStartingWithA() {
        List<String> names = studentService.getAllStudentsNamesStartingWithA();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/average-age-stream")
    public ResponseEntity<Double> getAverageAgeUsingStream() {
        Double averageAge = studentService.getAverageAgeUsingStream();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/print-parallel")
    public ResponseEntity<String> getAllStudentNameParallel() {
        List<Student> students = studentService.getAllStudents();

        if (students.size() < 6) {
            return ResponseEntity.badRequest().body("В базе данных должно быть минимум 6 студентов.");
        }

        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }).start();
        return ResponseEntity.ok("Список студентов выведен в консоль в параллельном режиме.");
    }

    @GetMapping("/print-synchronized")
    public ResponseEntity<String> printSynchronized(){
        List<Student> students = studentService.getAllStudents();

        if (students.size() < 6){
            return ResponseEntity.badRequest().body("В базе данных должно быть минимум 6 студентов.");
        }


        printTwoStudents(students.get(0), students.get(1));
        new Thread(() ->{
            printTwoStudents(students.get(2), students.get(3));
        }).start();
        new Thread(() ->{
            printTwoStudents(students.get(4), students.get(5));
        }).start();
        return ResponseEntity.ok("Список студентов выведен в консоль в синхронизированном режиме.");
    }
    private synchronized void printTwoStudents(Student s1, Student s2) {
        System.out.println(s1.getName());
        System.out.println(s2.getName());
    }
}