package ru.hogwarts.school.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
   Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //create
    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }
    //read
    public Student findStudent(Long id){
        logger.info("The method of searching for a student by ID was called");
        return studentRepository.findById(id).orElse(null);
    }

    //update
    public Student editStudent(Student student){
        logger.info("Was invoked method for edit student");
       return studentRepository.save(student);
    }

    //delete
    public void deleteStudent(Long id){
       logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    // фильтрация студентов по возрасту

    public Collection<Student> findStudentsByAge(int age) {

      logger.info("Was invoked method for find student by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max){
      logger.info("Was invoked method for find by student between");
        return studentRepository.findByAgeBetween(min, max);
    }
    public Integer getStudentsCount(){
       logger.info("Was invoked method getStudentsCount");
        return studentRepository.getStudentsCount();
    }

    public Double getAverageAge(){
       logger.info("Method to get average student age was called");
        return studentRepository.getAverageAge();
    }

    public Collection<Student> getLastFiveStudents() {

       logger.info("Method to get last five added students was called");
        return studentRepository.getLastFiveStudents();
    }

    public List<String> getAllStudentsNamesStartingWithA() {
        logger.info("Method to get all student names starting with A was called");

        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("А") || name.startsWith("а"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAgeUsingStream(){
        logger.info("Method to get average all student age ");
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }
}
