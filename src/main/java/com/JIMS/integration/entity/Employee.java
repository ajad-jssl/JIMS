package com.JIMS.integration.entity;


public class Employee {
    private String empcode;
    private String name;
    private String contact;
    private String email;

    // Constructor, getters, and setters
    public Employee(String empcode, String name, String contact, String email) {
        this.empcode = empcode;
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
