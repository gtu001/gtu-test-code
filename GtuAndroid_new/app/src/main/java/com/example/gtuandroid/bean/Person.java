package com.example.gtuandroid.bean;

public class Person {
    private String name;
    private int age;

    public Person() {
        name = "baby";
        age = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}