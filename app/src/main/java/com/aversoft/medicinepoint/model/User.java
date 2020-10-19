package com.aversoft.medicinepoint.model;

import java.io.Serializable;

public class User implements Serializable {
    String id, name, number, age, address, password, role, gender, shortCode;

    public User() {
    }

    public User(String id, String name, String number, String age, String address, String password, String role, String gender, String shortCode) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.age = age;
        this.address = address;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.shortCode = shortCode;
    }

    public User(String name, String number, String age, String address, String password, String role, String gender, String shortCode) {
        this.name = name;
        this.number = number;
        this.age = age;
        this.address = address;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.shortCode = shortCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getGender() {
        return gender;
    }

    public String getShortCode() {
        return shortCode;
    }
}
