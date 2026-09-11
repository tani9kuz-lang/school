package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultys = new HashMap<>();

    private long lastId = 0;
    //create
    public Faculty createFaculty (Faculty faculty){
        faculty.setId(++lastId);
        facultys.put(lastId, faculty);
        return faculty;
    }

    //read
    public Faculty findFaculty(long id){
        return facultys.get(id);
    }

    //update
    public  Faculty editFaculty(Faculty faculty){
        facultys.put(faculty.getId(), faculty);
        return faculty;
    }

    //delete
    public  Faculty deleteFaculty (long id){
        return facultys.remove(id);
    }

    // фильтрация факультетов по цвету
    public Collection<Faculty> findFacultyByColor(String color) {
        return facultys.values().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}
