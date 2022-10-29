package project.employee;

import java.io.Serializable;

public class Employee implements Serializable {
    private int id;
    private String name;
    private int age;
    private String address;
    private String job;
    private String level;
    private double salary;

    public Employee (int id, String name, int age, String address, String job, String level, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.job = job;
        this.level = level;
        this.salary = salary;
    }

    // Employee's id
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // Employee's name
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Employee's age;
    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    // Employee's address
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    // Employee's job
    public void setJob(String job) {
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    // Employee's level
    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    // Employee's salary
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    // toString
    public String toString() {
        return id + "@" + name + "@" + age + "@" + address + "@" + job + "@" + level + "@" + salary;
    }
}
