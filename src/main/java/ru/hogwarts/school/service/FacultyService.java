package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;


@Service
public class FacultyService {
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //create
    public Faculty createFaculty(Faculty faculty) {
       logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    //read
    public Faculty findFaculty(Long id) {
        logger.info("The method of searching for a faculty by ID");
        return facultyRepository.findById(id).orElse(null);
    }

    //update
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    //delete
    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty by ID");
        facultyRepository.deleteById(id);
    }

    // фильтрация факультетов по цвету
    public Collection<Faculty> findFacultyByColor(String color) {
       logger.info("Was invoked method for find faculty by color ");
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> findFacultyByNameOrColor(String searchParam) {
       logger.info("Was invoked method for find faculty by name or color");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(searchParam, searchParam);
    }

    public String getLongestFacultyName(){
        logger.info("Method to get longest faculty name was called");

        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("Факультеты не найдены");
    }
}
