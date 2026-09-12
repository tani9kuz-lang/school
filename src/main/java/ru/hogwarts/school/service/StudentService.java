package ru.hogwarts.school.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //create
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    //read
    public Student findStudent(long id){
        return studentRepository.findById(id).orElse(null);
    }

    //update
    public Student editStudent(Student student){
       return studentRepository.save(student);
    }

    //delete
    public void deleteStudent(long id){
        studentRepository.deleteById(id);
    }

    // фильтрация студентов по возрасту

    public Collection<Student> findStudentsByAge(int age) {
        return studentRepository.findByAge(age);
    }
}
