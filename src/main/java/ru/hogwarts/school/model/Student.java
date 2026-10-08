package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;



    @Check(constraints = "age >= 16" )
    @ColumnDefault("20")
    private Integer age;

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

    public Long getId() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
