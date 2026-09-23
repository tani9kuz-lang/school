package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private int age;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToOne(mappedBy = "student")
    @JsonIgnore
    private Avatar avatar;

    public Student() {
    }

    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    //Get
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public long getId() {
        return id;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    //Set
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
