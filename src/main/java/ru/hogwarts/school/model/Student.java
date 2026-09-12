package ru.hogwarts.school.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private  long id;

    private  String name;
    private  int age;


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

    //Set
    public void setName(String name){
        this.name = name;
    }

    public void setAge (int age){
        this.age = age;
    }

    public void setId (long id){
        this.id = id;
    }
}
