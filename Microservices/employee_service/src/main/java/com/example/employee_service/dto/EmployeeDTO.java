package com.example.employee_service.dto;




public class EmployeeDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getPassword() {
        return password; // Assurez-vous d'avoir cette méthode
    }

    public void setPassword(String password) {
        this.password = password; // Assurez-vous d'avoir cette méthode
    }
}
