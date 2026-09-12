package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;


@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //create
    public Faculty createFaculty (Faculty faculty){
        return facultyRepository.save(faculty);
    }

    //read
    public Faculty findFaculty(long id){
        return facultyRepository.findById(id).orElse(null);
    }

    //update
    public  Faculty editFaculty(Faculty faculty){
       return facultyRepository.save(faculty);
    }

    //delete
    public void deleteFaculty (long id){
        facultyRepository.deleteById(id);
    }

    // фильтрация факультетов по цвету
    public Collection<Faculty> findFacultyByColor(String color) {
        return facultyRepository.findByColor(color);
    }
}
