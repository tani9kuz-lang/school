package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();

    private long lastId = 0;

    //create
    public Student createStudent(Student student) {
        student.setId(++lastId);
        students.put(lastId, student);
        return student;
    }
    //read
    public Student findStudent(long id){
        return students.get(id);
    }

    //update
    public Student editStudent(Student student){
        students.put(student.getId(), student);
        return student;
    }

    //delete
    public Student deletwStudent(long id){
        return students.remove(id);
    }

    // фильтрациz студентов по возрасту
    public Collection<Student> findStudentsByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
